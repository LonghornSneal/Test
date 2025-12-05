package com.cosmobond.watchface

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class WatchFaceScreenshotTest {
    @get:Rule
    val paparazzi = Paparazzi()

    private val context: Context
        get() = paparazzi.context
    private val schema by lazy { CompanionUserStyle.createSchema(context.resources) }

    @Test
    fun snapshotInteractiveCosmicBlue() {
        val renderer = rendererFor(paletteOptionId = CompanionUserStyle.cosmicBlueOptionId, drawMode = DrawMode.INTERACTIVE)
        snapshotRenderer(renderer, "watchface_interactive_cosmic_blue")
    }

    @Test
    fun snapshotInteractiveOverlay() {
        val renderer =
            rendererFor(
                paletteOptionId = CompanionUserStyle.cosmicBlueOptionId,
                drawMode = DrawMode.INTERACTIVE,
            ) { it.debugForceGameState(GameState.Playing, streak = 6, misses = 1, cue = BeatChaseCue.Hit) }
        snapshotRenderer(renderer, "watchface_interactive_overlay")
    }

    @Test
    fun snapshotInteractiveStarlight() {
        val renderer = rendererFor(paletteOptionId = CompanionUserStyle.starlightOptionId, drawMode = DrawMode.INTERACTIVE)
        snapshotRenderer(renderer, "watchface_interactive_starlight")
    }

    @Test
    fun snapshotAmbient() {
        val renderer = rendererFor(paletteOptionId = CompanionUserStyle.cosmicBlueOptionId, drawMode = DrawMode.AMBIENT)
        snapshotRenderer(renderer, "watchface_ambient")
    }

    private fun rendererFor(
        paletteOptionId: UserStyleSetting.Option.Id,
        drawMode: DrawMode,
        configure: (CompanionWatchFaceRenderer) -> Unit = {},
    ): CompanionWatchFaceRenderer {
        val repository = CurrentUserStyleRepository(schema)
        applyPalette(repository, paletteOptionId)
        val watchState = createWatchState(isAmbient = drawMode == DrawMode.AMBIENT)
        val slotsManager = CompanionComplicationSlots.create(context, repository)
        val renderer =
            CompanionWatchFaceRenderer(
                context = context,
                surfaceHolder = FakeSurfaceHolder(SNAPSHOT_SIZE, SNAPSHOT_SIZE),
                currentUserStyleRepository = repository,
                watchState = watchState,
                complicationSlotsManager = slotsManager,
            )
        // renderer.renderParameters = renderParametersFor(drawMode) // Commented out due to access visibility issue
        configure(renderer)
        return renderer
    }

    private fun applyPalette(
        repository: CurrentUserStyleRepository,
        paletteOptionId: UserStyleSetting.Option.Id,
    ) {
        val paletteSetting =
            schema.userStyleSettings.filterIsInstance<UserStyleSetting.ListUserStyleSetting>().first {
                it.id == CompanionUserStyle.paletteSettingId
            }
        val option = paletteSetting.options.first { it.id == paletteOptionId }
        repository.updateUserStyle(UserStyle(mapOf(paletteSetting to option)))
    }

    /*
    private fun renderParametersFor(drawMode: DrawMode): RenderParameters {
        if (drawMode == DrawMode.AMBIENT) {
            return RenderParameters(
                DrawMode.AMBIENT,
                RenderParameters.DEFAULT_INTERACTIVE.watchFaceLayers,
                RenderParameters.DEFAULT_INTERACTIVE.highlightLayer,
                emptyMap(),
            )
        }
        return RenderParameters.DEFAULT_INTERACTIVE
    }
    */

    private fun snapshotRenderer(
        renderer: CompanionWatchFaceRenderer,
        name: String,
    ) {
        val bounds = Rect(0, 0, SNAPSHOT_SIZE, SNAPSHOT_SIZE)
        val view = WatchFacePreviewView(context, renderer, bounds, FIXED_TIME)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(SNAPSHOT_SIZE, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SNAPSHOT_SIZE, View.MeasureSpec.EXACTLY),
        )
        view.layout(0, 0, SNAPSHOT_SIZE, SNAPSHOT_SIZE)
        paparazzi.snapshot(view, name)
    }

    private class WatchFacePreviewView(
        context: Context,
        private val renderer: CompanionWatchFaceRenderer,
        private val bounds: Rect,
        private val dateTime: ZonedDateTime,
    ) : View(context) {
        override fun onDraw(canvas: Canvas) {
            renderer.render(canvas, bounds, dateTime)
        }
    }

    companion object {
        private const val SNAPSHOT_SIZE = 480
        private val FIXED_TIME: ZonedDateTime =
            ZonedDateTime.of(2025, 1, 1, 10, 15, 0, 0, ZoneId.of("UTC"))
    }
}
