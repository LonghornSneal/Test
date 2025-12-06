package com.cosmobond.watchface

import android.graphics.RectF
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private class FakeTempoSource(
    var bpm: Int? = 120,
    var playing: Boolean = true,
) : PlaybackTempoSource {
    override fun currentBpm(): Int? = bpm
    override fun isPlaying(): Boolean = playing
}

private class FakeClock(startMs: Long = 0L) : GameClock {
    var nowMs: Long = startMs
        private set
    override fun now(): Long = nowMs
    fun advanceBy(delta: Long) {
        nowMs += delta
    }
    fun setTo(value: Long) {
        nowMs = value
    }
}

private class FakeHaptics : Haptics {
    var ticks: Int = 0
        private set
    override fun tick() {
        ticks += 1
    }
}

class BeatChaseEngineTest {
    @Test
    fun beatWindowToleranceCapsAt140ms() {
        val tempo = FakeTempoSource(bpm = 50)
        val haptics = FakeHaptics()
        val clock = FakeClock()
        val engine = BeatChaseEngine(tempo, haptics, clock)

        engine.startIfTempoAvailable(clock.now())
        clock.advanceBy(3_000)
        engine.onTick(clock.now())

        val beat = engine.peekNearestBeat(clock.now())!!
        assertEquals(140L, beat.toleranceMs)
    }

    @Test
    fun onBeatHitIncrementsStreakAndTriggersHaptics() {
        val tempo = FakeTempoSource(bpm = 120)
        val haptics = FakeHaptics()
        val clock = FakeClock()
        val engine = BeatChaseEngine(tempo, haptics, clock)

        engine.startIfTempoAvailable(clock.now())
        clock.advanceBy(3_000)
        engine.onTick(clock.now())
        val beat = engine.peekNearestBeat(clock.now())!!

        clock.setTo(beat.centerMs)
        engine.onUserTap(clock.now(), allowHaptics = true)

        assertEquals(1, engine.streak)
        assertEquals(0, engine.misses)
        assertEquals(1, haptics.ticks)
        assertEquals(GameState.Playing, engine.state)
    }

    @Test
    fun reducedMotionSkipsHapticsButStillScoresHit() {
        val tempo = FakeTempoSource(bpm = 120)
        val haptics = FakeHaptics()
        val clock = FakeClock()
        val engine = BeatChaseEngine(tempo, haptics, clock)

        engine.startIfTempoAvailable(clock.now())
        clock.advanceBy(3_000)
        engine.onTick(clock.now())
        val beat = engine.peekNearestBeat(clock.now())!!

        engine.onUserTap(beat.centerMs, allowHaptics = false)

        assertEquals(1, engine.streak)
        assertEquals(0, haptics.ticks)
    }

    @Test
    fun missResetsStreakAndCountsTowardFailure() {
        val tempo = FakeTempoSource(bpm = 120)
        val haptics = FakeHaptics()
        val clock = FakeClock()
        val engine = BeatChaseEngine(tempo, haptics, clock)

        engine.startIfTempoAvailable(clock.now())
        clock.advanceBy(3_000)
        engine.onTick(clock.now())
        val beat = engine.peekNearestBeat(clock.now())!!

        val missTime = beat.centerMs + beat.toleranceMs + 200
        engine.onUserTap(missTime, allowHaptics = true)

        assertEquals(0, engine.streak)
        assertEquals(1, engine.misses)
        assertEquals(GameState.Playing, engine.state)
    }

    @Test
    fun threeMissesOrTimerExpiryEndsGameAsLoss() {
        val tempo = FakeTempoSource(bpm = 120)
        val haptics = FakeHaptics()
        val clock = FakeClock()
        val engine = BeatChaseEngine(tempo, haptics, clock)

        engine.startIfTempoAvailable(clock.now())
        clock.advanceBy(3_000)
        engine.onTick(clock.now())
        val beat = engine.peekNearestBeat(clock.now())!!

        val missTime = beat.centerMs + beat.toleranceMs + 300
        repeat(3) { engine.onUserTap(missTime + it, allowHaptics = true) }

        assertEquals(GameState.Finished, engine.state)
        assertEquals(BeatChaseCue.Lose, engine.cue)

        val tempo2 = FakeTempoSource(bpm = 120)
        val engine2 = BeatChaseEngine(tempo2, FakeHaptics(), FakeClock())
        engine2.startIfTempoAvailable(0)
        engine2.onTick(3_000)
        engine2.onTick(13_001)
        assertEquals(GameState.Finished, engine2.state)
        assertEquals(BeatChaseCue.Lose, engine2.cue)
    }

    @Test
    fun specialBeatAddsTimeAndCanWin() {
        val tempo = FakeTempoSource(bpm = 100)
        val haptics = FakeHaptics()
        val clock = FakeClock()
        val engine =
            BeatChaseEngine(
                tempo,
                haptics,
                clock,
                specialsEvery = 1,
                specialBonusCycleMs = listOf(3_000L),
                initialTimerMs = 10_000L,
                maxTimerMs = 12_000L,
            )

        engine.startIfTempoAvailable(clock.now())
        clock.advanceBy(3_000)
        engine.onTick(clock.now())
        val beat = engine.peekNearestBeat(clock.now())!!

        engine.onUserTap(beat.centerMs, allowHaptics = true)

        assertEquals(12_000L, engine.timerMs)
        assertEquals(GameState.Finished, engine.state)
        assertEquals(BeatChaseCue.Win, engine.cue)
    }

    @Test
    fun playbackStopsPausesGame() {
        val tempo = FakeTempoSource(bpm = 110)
        val haptics = FakeHaptics()
        val clock = FakeClock()
        val engine = BeatChaseEngine(tempo, haptics, clock)

        engine.startIfTempoAvailable(clock.now())
        clock.advanceBy(3_000)
        engine.onTick(clock.now())
        tempo.playing = false

        engine.onTick(clock.now())

        assertEquals(GameState.Paused, engine.state)
        assertEquals(BeatChaseCue.Pause, engine.cue)

        tempo.playing = true
        engine.resumeIfPossible(clock.now())
        assertEquals(GameState.Playing, engine.state)
    }

    @Test
    fun startRequiresTempoAndPlayback() {
        val tempo = FakeTempoSource(bpm = null, playing = false)
        val engine = BeatChaseEngine(tempo, FakeHaptics(), FakeClock())

        engine.startIfTempoAvailable(0)

        assertEquals(GameState.Paused, engine.state)
        assertEquals(BeatChaseCue.Pause, engine.cue)

        tempo.bpm = 110
        tempo.playing = true
        engine.startIfTempoAvailable(1_000)

        assertEquals(GameState.Countdown, engine.state)
        assertEquals(BeatChaseCue.Countdown, engine.cue)
    }

    @Test
    fun timerCapsAtMax() {
        val tempo = FakeTempoSource(bpm = 110)
        val haptics = FakeHaptics()
        val clock = FakeClock()
        val engine =
            BeatChaseEngine(
                tempo,
                haptics,
                clock,
                specialsEvery = 1,
                specialBonusCycleMs = listOf(50_000L),
                initialTimerMs = 190_000L,
                maxTimerMs = 200_000L,
            )

        engine.startIfTempoAvailable(clock.now())
        clock.advanceBy(3_000)
        engine.onTick(clock.now())
        val beat = engine.peekNearestBeat(clock.now())!!

        engine.onUserTap(beat.centerMs, allowHaptics = true)

        assertEquals(200_000L, engine.timerMs)
        assertEquals(BeatChaseCue.Win, engine.cue)
    }

    @Test
    fun overlayLayoutAvoidsComplications() {
        val complications =
            listOf(
                RectF(0.15f, 0.45f, 0.35f, 0.65f),
                RectF(0.65f, 0.45f, 0.85f, 0.65f),
                RectF(0.4f, 0.1f, 0.6f, 0.3f),
                RectF(0.4f, 0.7f, 0.6f, 0.9f),
            )

        assertTrue(BeatChaseOverlayDefaults.isClearOfComplications(complications))
    }

    @Test
    fun animationMappingHonorsStreakAndReducedMotion() {
        val hitAction =
            BeatBunnyVisuals.animationForCue(
                cue = BeatChaseCue.Hit,
                streak = 6,
                reducedMotion = false,
            )
        val hitReduced =
            BeatBunnyVisuals.animationForCue(
                cue = BeatChaseCue.Hit,
                streak = 6,
                reducedMotion = true,
            )
        val idleReduced =
            BeatBunnyVisuals.animationForCue(
                cue = BeatChaseCue.Idle,
                streak = 0,
                reducedMotion = true,
            )

        assertEquals(BeatBunnyAnimation.VoltagePopAction, hitAction)
        assertEquals(BeatBunnyAnimation.VoltagePop, hitReduced)
        assertEquals(BeatBunnyAnimation.AmbientBounceReduced, idleReduced)
        assertFalse(BeatBunnyVisuals.assetFor(hitAction).isBlank())
        assertFalse(BeatBunnyVisuals.assetFor(BeatBunnyAnimation.ClubCarousel).isBlank())
    }
}
