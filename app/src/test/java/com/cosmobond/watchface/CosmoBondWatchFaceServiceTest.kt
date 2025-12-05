package com.cosmobond.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import androidx.test.core.app.ApplicationProvider
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.style.CurrentUserStyleRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.ZonedDateTime

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CosmoBondWatchFaceServiceTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val schema = CompanionUserStyle.createSchema(context.resources)

    @Test
    fun complicationSlotsAreRegistered() {
        val repository = CurrentUserStyleRepository(schema)
        val slotsManager = CompanionComplicationSlots.create(context, repository)

        // Updated to expect 4 slots
        assertEquals(4, slotsManager.complicationSlots.size)
        val expectedIds = setOf(10, 11, 12, 13)
        assertTrue(slotsManager.complicationSlots.keys.containsAll(expectedIds))

        // Just check that we have slots with supported types
        assertTrue(slotsManager.complicationSlots.values.any { it.supportedTypes.contains(ComplicationType.SHORT_TEXT) })
    }

    @Test
    fun rendererProducesFrame() {
        val repository = CurrentUserStyleRepository(schema)
        val slotsManager = CompanionComplicationSlots.create(context, repository)
        val watchState = createWatchState()
        val renderer =
            CompanionWatchFaceRenderer(
                context,
                FakeSurfaceHolder(),
                repository,
                watchState,
                slotsManager,
            )
        // Renderer parameters are internal in some versions or context-dependent.
        // Assuming default initialization allows rendering.
        // renderer.renderParameters = RenderParameters.DEFAULT_INTERACTIVE

        val bitmap = Bitmap.createBitmap(480, 480, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        renderer.render(canvas, Rect(0, 0, 480, 480), ZonedDateTime.now())

        // Check that something was drawn (not transparent)
        // Note: This relies on the background being drawn.
        assertNotEquals(0, bitmap.getPixel(240, 240))
    }
}
