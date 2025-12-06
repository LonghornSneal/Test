package com.cosmobond.watchface

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.SurfaceHolder
import androidx.annotation.VisibleForTesting
import android.graphics.Typeface
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.TapEvent
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
    private val enableMediaMonitor: Boolean = true,
) : Renderer.CanvasRenderer(
        surfaceHolder,
        currentUserStyleRepository,
        watchState,
        CanvasType.HARDWARE,
        INTERACTIVE_FRAME_MS,
        false,
    ) {
    private val settingsRepository = CompanionSettingsRepository(context)
    private var settings = settingsRepository.current()

    @VisibleForTesting internal val tempoSource = MutablePlaybackTempoSource()
    private val haptics: Haptics =
        ToggleableHaptics(
            context,
        ) {
            settings.hapticsEnabled && !settings.reducedMotion
        }
    private val layoutPreferencesRepository = LayoutPreferencesRepository(context)
    private var layoutPreferences = layoutPreferencesRepository.current()
    @VisibleForTesting internal val beatEngine =
        BeatChaseEngine(
            tempoSource,
            haptics,
            SystemGameClock(),
        )
    @VisibleForTesting internal var lastAnimationAsset: String? = null
    private val mediaSessionMonitor =
        if (enableMediaMonitor) {
            MediaSessionPlaybackMonitor(
                context = context,
                tempoSink = tempoSource,
                onPlaybackUpdate = ::onPlaybackSnapshot,
            )
        } else {
            null
        }
    private val petStats = CompanionPetStats()
    private var lastFrameTimeMs: Long? = null
    private var lastAmbient = false
    private val lastRenderBounds = Rect()

    private val timePaint =
        Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 64f
            isAntiAlias = true
        }
    private val datePaint =
        Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 28f
            isAntiAlias = true
        }
    private val musicPaint =
        Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 22f
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

    private val statsPaint =
        Paint().apply {
            textAlign = Paint.Align.RIGHT
            textSize = 24f
            isAntiAlias = true
        }

    init {
        complicationSlotsManager.watchState = watchState
        settingsRepository.addListener { newSettings ->
            settings = newSettings
        }
        layoutPreferencesRepository.addListener { newPrefs ->
            layoutPreferences = newPrefs
        }
        mediaSessionMonitor?.start()
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
    ) {
        lastRenderBounds.set(bounds)
        val nowMs = System.currentTimeMillis()
        val isAmbient = renderParameters.drawMode == DrawMode.AMBIENT
        if (isAmbient != lastAmbient) {
            beatEngine.onAmbientModeChanged(isAmbient)
            if (!isAmbient && tempoSource.isReady()) {
                beatEngine.resumeIfPossible(nowMs)
            }
            lastAmbient = isAmbient
        }
        if (!isAmbient && beatEngine.state == GameState.Idle && tempoSource.isReady()) {
            beatEngine.startIfTempoAvailable(nowMs)
        }
        beatEngine.onTick(nowMs)
        val delta = lastFrameTimeMs?.let { nowMs - it } ?: 0
        lastFrameTimeMs = nowMs
        petStats.tick(delta, !isAmbient && beatEngine.state == GameState.Playing)
        lastAnimationAsset =
            if (!isAmbient) {
                BeatBunnyVisuals.assetFor(
                    BeatBunnyVisuals.animationForCue(
                        cue = beatEngine.cue,
                        streak = beatEngine.streak,
                        reducedMotion = settings.reducedMotion,
                    ),
                )
            } else {
                null
            }

        val currentStyle = currentUserStyleRepository.userStyle.value
        backgroundPaint.color = CompanionPalette.backgroundFor(renderParameters.drawMode)
        timePaint.color =
            CompanionPalette.accentFor(
                currentStyle,
                renderParameters.drawMode,
            )
        datePaint.color = timePaint.color
        musicPaint.color = timePaint.color
        val lowBit = watchState.hasLowBitAmbient || watchState.hasBurnInProtection
        timePaint.isAntiAlias = !(isAmbient && lowBit)
        datePaint.isAntiAlias = !(isAmbient && lowBit)
        musicPaint.isAntiAlias = !(isAmbient && lowBit)
        timePaint.typeface =
            when (layoutPreferences.timeFont) {
                FontVariant.MONO -> Typeface.MONOSPACE
                FontVariant.SERIF -> Typeface.SERIF
                else -> Typeface.DEFAULT
            }
        datePaint.typeface =
            when (layoutPreferences.dateFont) {
                FontVariant.MONO -> Typeface.MONOSPACE
                FontVariant.SERIF -> Typeface.SERIF
                else -> Typeface.DEFAULT
            }
        musicPaint.typeface = Typeface.SANS_SERIF
        timePaint.textSize = layoutPreferences.timeTextSizeSp
        datePaint.textSize = layoutPreferences.dateTextSizeSp
        musicPaint.textSize = layoutPreferences.musicTextSizeSp
        // Keep sizes reasonable in ambient
        if (isAmbient) {
            timePaint.textSize *= 0.9f
            datePaint.textSize *= 0.9f
            musicPaint.textSize *= 0.9f
            }

        canvas.drawRect(bounds, backgroundPaint)
        val timePos = layoutPreferences.timePosition
        val datePos = layoutPreferences.datePosition
        val musicPos = layoutPreferences.musicPosition
        val timeX = bounds.left + bounds.width() * timePos.x.coerceIn(0f, 1f)
        val timeY = bounds.top + bounds.height() * timePos.y.coerceIn(0f, 1f)
        canvas.drawText(
            CompanionTimeFormatter.formatTime(
                zonedDateTime,
                layoutPreferences.showSeconds && !isAmbient,
                layoutPreferences.use24Hour,
            ),
            timeX,
            timeY,
            timePaint,
        )
        if (layoutPreferences.showDate) {
            val dateX = bounds.left + bounds.width() * datePos.x.coerceIn(0f, 1f)
            val dateY = bounds.top + bounds.height() * datePos.y.coerceIn(0f, 1f)
            canvas.drawText(
                CompanionDateFormatter.formatDate(zonedDateTime, layoutPreferences.dateFormat),
                dateX,
                dateY,
                datePaint,
            )
        }
        if (!isAmbient) {
            val musicX = bounds.left + bounds.width() * musicPos.x.coerceIn(0f, 1f)
            val musicY = bounds.top + bounds.height() * musicPos.y.coerceIn(0f, 1f)
            canvas.drawText("Spotify", musicX, musicY, musicPaint)
        }

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
            drawPetStats(canvas, bounds)
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

    private fun drawPetStats(
        canvas: Canvas,
        bounds: Rect,
    ) {
        statsPaint.color = CompanionPalette.accentFor(currentUserStyleRepository.userStyle.value, renderParameters.drawMode)
        val text = "H ${petStats.hunger}%  E ${petStats.energy}%  M ${petStats.mood}%"
        val x = bounds.left + bounds.width() * 0.88f
        val y = bounds.top + bounds.height() * 0.22f
        canvas.drawText(text, x, y, statsPaint)
    }

    private fun drawBeatOverlay(
        canvas: Canvas,
        bounds: Rect,
        userStyle: androidx.wear.watchface.style.UserStyle,
    ) {
        if (beatEngine.state == GameState.Idle || beatEngine.state == GameState.Paused) return
        if (!BeatChaseOverlayDefaults.isClearOfComplications(CompanionComplicationSlots.normalizedBounds())) {
            return
        }
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
        if (renderParameters.drawMode == DrawMode.AMBIENT) return
        val tappedSlot =
            runCatching {
                complicationSlotsManager.getComplicationSlotAt(tapEvent.xPos, tapEvent.yPos)
            }.getOrNull()
        if (tappedSlot != null) {
            return
        }
        if (beatEngine.state != GameState.Playing) return
        val overlayBounds = BeatChaseOverlayDefaults.layout.tapTarget.bounds().scale(lastRenderBounds)
        if (!overlayBounds.contains(tapEvent.xPos.toFloat(), tapEvent.yPos.toFloat())) return
        val allowHaptics = settings.hapticsEnabled && !settings.reducedMotion
        beatEngine.onUserTap(tapEvent.tapTime.toEpochMilli(), allowHaptics = allowHaptics)
    }

    private fun onPlaybackSnapshot(snapshot: PlaybackSnapshot) {
        tempoSource.currentBpm = snapshot.bpm
        tempoSource.playing = snapshot.isPlaying
        if (lastAmbient) return
        if (!snapshot.isPlaying || snapshot.bpm == null) {
            if (beatEngine.state != GameState.Idle) {
                beatEngine.onPlaybackStopped()
            }
            return
        }
        val now = System.currentTimeMillis()
        when (beatEngine.state) {
            GameState.Idle,
            GameState.Finished,
            -> beatEngine.startIfTempoAvailable(now)
            GameState.Paused -> beatEngine.resumeIfPossible(now)
            else -> Unit
        }
    }

    override fun onDestroy() {
        mediaSessionMonitor?.stop()
        settingsRepository.close()
        layoutPreferencesRepository.close()
        super.onDestroy()
    }
}

internal class MutablePlaybackTempoSource : PlaybackTempoSource {
    var currentBpm: Int? = null
    var playing: Boolean = false
    override fun currentBpm(): Int? = currentBpm
    override fun isPlaying(): Boolean = playing

    fun isReady(): Boolean = playing && currentBpm != null
}

private class ToggleableHaptics(
    private val context: Context,
    private val enabled: () -> Boolean,
) : Haptics {
    private val vibrator: Vibrator? = context.getSystemService(Vibrator::class.java)

    override fun tick() {
        if (!enabled()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(20)
        }
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
