package com.cosmobond.watchface

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasComplication
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import java.time.ZonedDateTime

private const val LEFT_SLOT_ID = 10
private const val RIGHT_SLOT_ID = 11
private const val INTERACTIVE_FRAME_MS = 1000L
private const val BACKGROUND_COLOR = 0xFF0A0D18.toInt()
private const val AMBIENT_BACKGROUND_COLOR = 0xFF000000.toInt()

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
            color = BACKGROUND_COLOR
        }

    init {
        complicationSlotsManager.watchState = watchState
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
    ) {
        backgroundPaint.color = CompanionPalette.backgroundFor(renderParameters.drawMode)
        timePaint.color =
            CompanionPalette.accentFor(
                currentUserStyleRepository.userStyle.value,
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

        if (!isAmbient) {
            complicationSlotsManager.complicationSlots.values.forEach { slot ->
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

internal object CompanionComplicationSlots {
    private val supportedTypes =
        listOf(
            ComplicationType.SHORT_TEXT,
            ComplicationType.MONOCHROMATIC_IMAGE,
            ComplicationType.SMALL_IMAGE,
        )

    fun create(
        context: Context,
        currentUserStyleRepository: CurrentUserStyleRepository,
    ): ComplicationSlotsManager {
        val leftSlot =
            ComplicationSlot.createRoundRectComplicationSlotBuilder(
                LEFT_SLOT_ID,
                PlaceholderCanvasComplication.factory(),
                supportedTypes,
                DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_DAY_OF_WEEK,
                    ComplicationType.SHORT_TEXT,
                ),
                ComplicationSlotBounds(RectF(0.23f, 0.58f, 0.39f, 0.78f)),
            )
                .setNameResourceId(R.string.complication_left_name)
                .setScreenReaderNameResourceId(R.string.complication_left_name)
                .build()

        val rightSlot =
            ComplicationSlot.createRoundRectComplicationSlotBuilder(
                RIGHT_SLOT_ID,
                PlaceholderCanvasComplication.factory(),
                supportedTypes,
                DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_STEP_COUNT,
                    ComplicationType.SHORT_TEXT,
                ),
                ComplicationSlotBounds(RectF(0.61f, 0.58f, 0.77f, 0.78f)),
            )
                .setNameResourceId(R.string.complication_right_name)
                .setScreenReaderNameResourceId(R.string.complication_right_name)
                .build()

        return ComplicationSlotsManager(
            listOf(leftSlot, rightSlot),
            currentUserStyleRepository,
        )
    }
}

internal object CompanionUserStyle {
    val paletteSettingId = UserStyleSetting.Id("palette")

    internal val cosmicBlueOptionId = UserStyleSetting.Option.Id("cosmic_blue")
    internal val starlightOptionId = UserStyleSetting.Option.Id("starlight")

    private var paletteSetting: UserStyleSetting.ListUserStyleSetting? = null

    fun createSchema(resources: Resources): UserStyleSchema {
        val palette =
            UserStyleSetting.ListUserStyleSetting(
                paletteSettingId,
                resources,
                R.string.user_style_palette,
                R.string.user_style_palette_description,
                null,
                listOf(
                    UserStyleSetting.ListUserStyleSetting.ListOption(
                        cosmicBlueOptionId,
                        resources,
                        R.string.palette_cosmic_blue,
                        null,
                    ),
                    UserStyleSetting.ListUserStyleSetting.ListOption(
                        starlightOptionId,
                        resources,
                        R.string.palette_starlight,
                        null,
                    ),
                ),
                listOf(
                    WatchFaceLayer.BASE,
                    WatchFaceLayer.COMPLICATIONS,
                    WatchFaceLayer.COMPLICATIONS_OVERLAY,
                ),
            )
        paletteSetting = palette
        return UserStyleSchema(listOf(palette))
    }

    fun accentOption(userStyle: UserStyle): UserStyleSetting.Option? {
        val setting = paletteSetting ?: return null
        return userStyle[setting]
    }
}

internal object CompanionPalette {
    private const val COSMIC_BLUE = 0xFFA6B6FF.toInt()
    private const val STARLIGHT = 0xFFF5E5B9.toInt()
    private const val AMBIENT_ACCENT = 0xFFB0B0B0.toInt()

    fun accentFor(
        userStyle: UserStyle,
        drawMode: DrawMode = DrawMode.INTERACTIVE,
    ): Int {
        if (drawMode == DrawMode.AMBIENT) return AMBIENT_ACCENT
        return when (CompanionUserStyle.accentOption(userStyle)?.id) {
            CompanionUserStyle.starlightOptionId -> STARLIGHT
            else -> COSMIC_BLUE
        }
    }

    fun backgroundFor(drawMode: DrawMode): Int {
        return if (drawMode == DrawMode.AMBIENT) AMBIENT_BACKGROUND_COLOR else BACKGROUND_COLOR
    }
}

internal class PlaceholderCanvasComplication : CanvasComplication {
    private val outlinePaint =
        Paint().apply {
            color = Color.argb(70, 166, 182, 255)
            style = Paint.Style.STROKE
            strokeWidth = 4f
            isAntiAlias = true
        }
    private val highlightPaint =
        Paint().apply {
            color = Color.argb(120, 166, 182, 255)
            style = Paint.Style.STROKE
            strokeWidth = 6f
            isAntiAlias = true
        }
    private var data: ComplicationData = NoDataComplicationData()

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        renderParameters: RenderParameters,
        slotId: Int,
    ) {
        canvas.drawRoundRect(RectF(bounds), 12f, 12f, outlinePaint)
    }

    override fun drawHighlight(
        canvas: Canvas,
        bounds: Rect,
        boundsType: Int,
        zonedDateTime: ZonedDateTime,
        slotId: Int,
    ) {
        canvas.drawRoundRect(RectF(bounds), 12f, 12f, highlightPaint)
    }

    override fun getData(): ComplicationData = data

    override fun loadData(
        complicationData: ComplicationData,
        loadDrawablesAsynchronous: Boolean,
    ) {
        data = complicationData
    }

    companion object {
        fun factory(): CanvasComplicationFactory =
            CanvasComplicationFactory { _, _ ->
                PlaceholderCanvasComplication()
            }
    }
}
