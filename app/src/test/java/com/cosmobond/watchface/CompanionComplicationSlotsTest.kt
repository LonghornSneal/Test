package com.cosmobond.watchface

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.style.CurrentUserStyleRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CompanionComplicationSlotsTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val schema = CompanionUserStyle.createSchema(context.resources)

    @Test
    fun defaultSlotsHaveExpectedIdsAndPolicies() {
        val slotsManager = CompanionComplicationSlots.create(context, CurrentUserStyleRepository(schema))
        val slots = slotsManager.complicationSlots
        assertEquals(setOf(10, 11), slots.keys)
        val left = slots[10]!!
        val right = slots[11]!!
        assertTrue(left.supportedTypes.contains(ComplicationType.SHORT_TEXT))
        assertTrue(right.supportedTypes.contains(ComplicationType.SHORT_TEXT))
    }
}
