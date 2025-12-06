package com.cosmobond.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import androidx.test.core.app.ApplicationProvider
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.TapEvent
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CompanionWatchFaceRendererTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val schema = CompanionUserStyle.createSchema(context.resources)

    @Test
    fun tapOutsideOverlayIsIgnored() {
        val repository = CurrentUserStyleRepository(schema)
        val slotsManager = CompanionComplicationSlots.create(context, repository)
        val renderer =
            CompanionWatchFaceRenderer(
                context = context,
                surfaceHolder = FakeSurfaceHolder(480, 480),
                currentUserStyleRepository = repository,
                watchState = createWatchState(),
                complicationSlotsManager = slotsManager,
                enableMediaMonitor = false,
            )
        renderer.tempoSource.currentBpm = 120
        renderer.tempoSource.playing = true
        renderer.beatEngine.startIfTempoAvailable(0)
        renderer.debugForceGameState(GameState.Playing, streak = 0, misses = 0, cue = BeatChaseCue.Idle)
        val bounds = Rect(0, 0, 480, 480)
        val canvas = Canvas(Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888))

        renderer.render(canvas, bounds, ZonedDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC")))
        renderer.tempoSource.playing = true
        renderer.debugForceGameState(GameState.Playing, streak = 0, misses = 0, cue = BeatChaseCue.Idle)

        renderer.handleTap(TapEvent(0, 0, Instant.now()))
        assertEquals(0, renderer.beatEngine.misses)

        val tapTarget = BeatChaseOverlayDefaults.layout.tapTarget.bounds()
        val centerX = (tapTarget.centerX() * bounds.width()).toInt()
        val centerY = (tapTarget.centerY() * bounds.height()).toInt()
        renderer.handleTap(TapEvent(centerX, centerY, Instant.now()))

        assertEquals(1, renderer.beatEngine.misses + renderer.beatEngine.streak)
    }
}
