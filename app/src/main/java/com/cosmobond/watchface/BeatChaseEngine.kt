package com.cosmobond.watchface

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import androidx.annotation.VisibleForTesting

internal enum class GameState { Idle, Countdown, Playing, Paused, Finished }

internal data class BeatWindow(
    val centerMs: Long,
    val toleranceMs: Long,
    val bonusMs: Long = 0,
) {
    val isSpecial: Boolean get() = bonusMs > 0
}

internal enum class BeatChaseCue {
    Idle,
    Countdown,
    Hit,
    Miss,
    Win,
    Lose,
    Pause,
}

internal interface PlaybackTempoSource {
    fun currentBpm(): Int?
    fun isPlaying(): Boolean
}

internal interface GameClock {
    fun now(): Long
}

internal interface Haptics {
    fun tick()
}

internal class BeatChaseEngine(
    private val tempoSource: PlaybackTempoSource,
    private val haptics: Haptics,
    private val clock: GameClock,
    private val specialsEvery: Int = 8,
    private val specialBonusCycleMs: List<Long> = listOf(1_000L, 2_000L, 3_000L),
    private val initialTimerMs: Long = 10_000L,
    private val maxTimerMs: Long = 200_000L,
    private val countdownMs: Long = 3_000L,
    private val maxMisses: Int = 3,
) {
    var state: GameState = GameState.Idle
        private set
    var streak: Int = 0
        private set
    var misses: Int = 0
        private set
    var timerMs: Long = initialTimerMs
        internal set
    var cue: BeatChaseCue = BeatChaseCue.Idle
        private set

    private var beatWindows: List<BeatWindow> = emptyList()
    private var countdownStartMs: Long? = null
    private var playStartMs: Long? = null
    private var lastTickMs: Long? = null

    fun startIfTempoAvailable(now: Long = clock.now()) {
        val bpm = tempoSource.currentBpm() ?: return pauseToSilence()
        if (!tempoSource.isPlaying()) return pauseToSilence()
        timerMs = initialTimerMs
        streak = 0
        misses = 0
        countdownStartMs = now
        playStartMs = now + countdownMs
        beatWindows = buildBeatWindows(playStartMs!!, bpm, maxTimerMs)
        state = GameState.Countdown
        cue = BeatChaseCue.Countdown
        lastTickMs = now
    }

    fun onPlaybackStopped() {
        pauseToSilence()
    }

    fun onAmbientModeChanged(isAmbient: Boolean) {
        if (isAmbient) pauseToSilence()
    }

    fun resumeIfPossible(now: Long = clock.now()) {
        if (state != GameState.Paused) return
        val bpm = tempoSource.currentBpm() ?: return
        if (!tempoSource.isPlaying()) return
        if (beatWindows.isEmpty()) {
            val start = now + countdownMs
            beatWindows = buildBeatWindows(start, bpm, maxTimerMs)
            playStartMs = start
            countdownStartMs = now
            state = GameState.Countdown
            cue = BeatChaseCue.Countdown
        } else if (countdownStartMs != null && now - countdownStartMs!! < countdownMs) {
            state = GameState.Countdown
            cue = BeatChaseCue.Countdown
        } else {
            state = GameState.Playing
            cue = BeatChaseCue.Idle
        }
        lastTickMs = now
    }

    fun onTick(now: Long = clock.now()) {
        when (state) {
            GameState.Countdown -> {
                val started = countdownStartMs ?: return
                if (now - started >= countdownMs) {
                    state = GameState.Playing
                    lastTickMs = now
                }
            }
            GameState.Playing -> {
                if (!tempoSource.isPlaying()) {
                    pauseToSilence()
                    return
                }
                val previousTick = lastTickMs ?: now
                val delta = max(0, now - previousTick)
                timerMs = max(0, timerMs - delta)
                lastTickMs = now
                if (timerMs == 0L || misses >= maxMisses) finish(win = false)
            }
            else -> {
                lastTickMs = now
            }
        }
    }

    fun onUserTap(now: Long = clock.now(), reducedMotion: Boolean) {
        if (state != GameState.Playing) return
        if (!tempoSource.isPlaying()) {
            pauseToSilence()
            return
        }
        val window = nearestBeat(now)
        if (window != null && abs(now - window.centerMs) <= window.toleranceMs) {
            streak += 1
            misses = 0
            if (window.isSpecial) timerMs = min(maxTimerMs, timerMs + window.bonusMs)
            if (!reducedMotion) haptics.tick()
            cue = BeatChaseCue.Hit
            if (timerMs >= maxTimerMs) finish(win = true)
        } else {
            streak = 0
            misses += 1
            cue = BeatChaseCue.Miss
            if (misses >= maxMisses) finish(win = false)
        }
    }

    private fun finish(win: Boolean) {
        state = GameState.Finished
        cue = if (win) BeatChaseCue.Win else BeatChaseCue.Lose
    }

    private fun pauseToSilence() {
        state = GameState.Paused
        cue = BeatChaseCue.Pause
        lastTickMs = null
    }

    private fun buildBeatWindows(
        startMs: Long,
        bpm: Int,
        durationMs: Long,
    ): List<BeatWindow> {
        val beatInterval = max(50L, (60000.0 / bpm).toLong())
        val tolerance = min(140L, (beatInterval * 0.18).toLong())
        val specials = if (specialsEvery <= 0) Int.MAX_VALUE else specialsEvery
        val cycle = if (specialBonusCycleMs.isEmpty()) listOf(0L) else specialBonusCycleMs
        val windows = mutableListOf<BeatWindow>()
        var t = startMs
        var index = 0
        while (t <= startMs + durationMs) {
            val bonus =
                if ((index + 1) % specials == 0) cycle[index % cycle.size] else 0L
            windows += BeatWindow(t, tolerance, bonus)
            t += beatInterval
            index += 1
        }
        return windows
    }

    private fun nearestBeat(now: Long): BeatWindow? =
        beatWindows.minByOrNull { abs(now - it.centerMs) }

    internal fun peekNearestBeat(now: Long): BeatWindow? = nearestBeat(now)

    @VisibleForTesting
    fun forceState(
        state: GameState,
        streak: Int = this.streak,
        misses: Int = this.misses,
        timerMs: Long = this.timerMs,
        cue: BeatChaseCue = this.cue,
    ) {
        this.state = state
        this.streak = streak
        this.misses = misses
        this.timerMs = timerMs
        this.cue = cue
    }
}

internal class SystemGameClock : GameClock {
    override fun now(): Long = System.currentTimeMillis()
}
