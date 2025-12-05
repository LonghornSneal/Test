package com.cosmobond.watchface

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.SurfaceHolder
import androidx.annotation.VisibleForTesting
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.TapEvent
import java.time.Instant
import java.time.ZonedDateTime

private const val INTERACTIVE_FRAME_MS = 1000L
private const val STREAK_ARC_MAX = 10f
private const val MISS_DOT_ALPHA = 180

@SuppressLint("RestrictedApi")
internal class CompanionWatchFaceRenderer(
    context: Context,
    surfaceHolder: SurfaceHolder,
    private val currentUserStyleRepository: CurrentUserStyleRepository,
    private val watchState: WatchState,
    private val complicationSlotsManager: ComplicationSlotsManager,
) : Renderer.CanvasRenderer(
        surfaceHolder,
        currentUserStyleRepository,
        watchState,
        CanvasType.HARDWARE,
        INTERACTIVE_FRAME_MS,
        false,
    ) {
    @VisibleForTesting internal val tempoSource = MutablePlaybackTempoSource()
    @VisibleForTesting internal val beatEngine =
        BeatChaseEngine(
            tempoSource,
            NoOpHaptics(),
            SystemGameClock(),
        )

    private val timePaint =
        Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 64f
            isAntiAlias = true
        }

    private val backgroundPaint =
        Paint().apply {
            color = CompanionPalette.backgroundFor(DrawMode.INTERACTIVE)
        }

    private val overlayRingPaint =
        Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    private val overlayArcPaint =
        Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    private val overlayDotPaint =
        Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

    private var lastAmbient = false

    init {
        complicationSlotsManager.watchState = watchState
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
    ) {
        val nowMs = System.currentTimeMillis()
        val isAmbient = renderParameters.drawMode == DrawMode.AMBIENT
        if (isAmbient != lastAmbient) {
            beatEngine.onAmbientModeChanged(isAmbient)
            lastAmbient = isAmbient
        }
        tempoSource.playing = !isAmbient
        if (tempoSource.currentBpm == null && !isAmbient) {
            tempoSource.currentBpm = 120 // placeholder until media metadata is wired
        }
        if (!isAmbient && beatEngine.state == GameState.Idle) {
            beatEngine.startIfTempoAvailable(nowMs)
        }
        beatEngine.onTick(nowMs)

        val currentStyle = currentUserStyleRepository.userStyle.value
        backgroundPaint.color = CompanionPalette.backgroundFor(renderParameters.drawMode)
        timePaint.color =
            CompanionPalette.accentFor(
                currentStyle,
                renderParameters.drawMode,
            )
        val lowBit = watchState.hasLowBitAmbient || watchState.hasBurnInProtection
        timePaint.isAntiAlias = !(isAmbient && lowBit)

        canvas.drawRect(bounds, backgroundPaint)
        canvas.drawText(
            CompanionTimeFormatter.formatTime(zonedDateTime),
            bounds.exactCenterX(),
            bounds.centerY().toFloat(),
            timePaint,
        )

        // Draw complications
        for ((_, slot) in complicationSlotsManager.complicationSlots) {
            if (slot.enabled) {
                // In ambient mode, we might want to hide complications or draw them differently.
                // For now, let's draw them if not ambient, or if they are ambient-compatible.
                // ComplicationDrawable handles ambient mode internal state if passed correctly.

                // Note: The ComplicationDrawable needs to be updated with the correct style
                // This is usually done by listening to style changes or updating before render.
                // For simplicity here, we assume default styling or that it's handled by the factory.

                slot.render(canvas, zonedDateTime, renderParameters)
            }
        }

        if (!isAmbient) {
            drawBeatOverlay(canvas, bounds, currentStyle)
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
    ) {
        complicationSlotsManager.complicationSlots.values.forEach { slot ->
            slot.renderHighlightLayer(canvas, zonedDateTime, renderParameters)
        }
    }

    override fun shouldAnimate(): Boolean = renderParameters.drawMode == DrawMode.INTERACTIVE

    @VisibleForTesting
    fun debugForceGameState(
        state: GameState,
        streak: Int,
        misses: Int,
        cue: BeatChaseCue,
    ) {
        beatEngine.forceState(
            state = state,
            streak = streak,
            misses = misses,
            timerMs = beatEngine.timerMs,
            cue = cue,
        )
    }

    private fun drawBeatOverlay(
        canvas: Canvas,
        bounds: Rect,
        userStyle: androidx.wear.watchface.style.UserStyle,
    ) {
        val accent = CompanionPalette.accentFor(userStyle, renderParameters.drawMode)
        val missColor = 0xFFFF6B6B.toInt()
        val successColor = accent
        val cue = beatEngine.cue

        val overlay = BeatChaseOverlayDefaults.layout
        val tapTarget = overlay.tapTarget.bounds().scale(bounds)
        val arcBounds = overlay.streakArc.bounds().scale(bounds)
        val missDots = overlay.missDots.bounds().map { it.scale(bounds) }
        val stroke = overlay.tapTarget.strokeFraction * bounds.width()

        overlayRingPaint.color =
            when (cue) {
                BeatChaseCue.Hit -> successColor
                BeatChaseCue.Miss -> missColor
                BeatChaseCue.Countdown -> successColor
                BeatChaseCue.Win -> successColor
                BeatChaseCue.Lose -> missColor
                else -> successColor
            }
        overlayRingPaint.strokeWidth = stroke

        canvas.drawOval(tapTarget, overlayRingPaint)

        val streakProgress =
            (beatEngine.streak.toFloat().coerceAtMost(STREAK_ARC_MAX) / STREAK_ARC_MAX) * 360f
        overlayArcPaint.color = successColor
        overlayArcPaint.strokeWidth = overlay.streakArc.thicknessFraction * bounds.width()
        canvas.drawArc(arcBounds, -90f, streakProgress, false, overlayArcPaint)

        overlayDotPaint.color = missColor
        overlayDotPaint.alpha = MISS_DOT_ALPHA
        missDots.forEachIndexed { index, rect ->
            if (index < beatEngine.misses) {
                canvas.drawOval(rect, overlayDotPaint)
            }
        }
    }

    fun handleTap(tapEvent: TapEvent) {
        val reducedMotion = renderParameters.drawMode == DrawMode.AMBIENT
        beatEngine.onUserTap(tapEvent.tapTime.toEpochMilli(), reducedMotion = reducedMotion)
    }
}

internal class MutablePlaybackTempoSource : PlaybackTempoSource {
    var currentBpm: Int? = null
    var playing: Boolean = false
    override fun currentBpm(): Int? = currentBpm
    override fun isPlaying(): Boolean = playing
}

private class NoOpHaptics : Haptics {
    override fun tick() {
        // Hook haptic feedback (Vibrator/VibratorManager) when hardware is available.
    }
}

private fun RectF.scale(bounds: Rect): RectF {
    return RectF(
        bounds.left + this.left * bounds.width(),
        bounds.top + this.top * bounds.height(),
        bounds.left + this.right * bounds.width(),
        bounds.top + this.bottom * bounds.height(),
    )
}
