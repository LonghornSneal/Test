package com.cosmobond.watchface

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

private const val INTERACTIVE_FRAME_MS = 1000L

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

    init {
        complicationSlotsManager.watchState = watchState
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
    ) {
        val currentStyle = currentUserStyleRepository.userStyle.value
        backgroundPaint.color = CompanionPalette.backgroundFor(renderParameters.drawMode)
        timePaint.color =
            CompanionPalette.accentFor(
                currentStyle,
                renderParameters.drawMode,
            )
        val isAmbient = renderParameters.drawMode == DrawMode.AMBIENT
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
}
