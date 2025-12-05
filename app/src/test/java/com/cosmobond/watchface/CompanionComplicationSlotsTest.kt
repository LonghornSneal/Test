package com.cosmobond.watchface

import android.content.Context
import androidx.test.core.app.ApplicationProvider
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

        // Expect 4 slots: Left(10), Right(11), Top(12), Bottom(13)
        assertEquals(setOf(10, 11, 12, 13), slots.keys)

        val left = slots[10]!!
        val right = slots[11]!!
        val top = slots[12]!!
        val bottom = slots[13]!!

        assertTrue("Left slot supports SHORT_TEXT", left.supportedTypes.contains(ComplicationType.SHORT_TEXT))
        assertTrue("Right slot supports SHORT_TEXT", right.supportedTypes.contains(ComplicationType.SHORT_TEXT))
        assertTrue("Top slot supports RANGED_VALUE", top.supportedTypes.contains(ComplicationType.RANGED_VALUE))
        assertTrue("Bottom slot supports SHORT_TEXT", bottom.supportedTypes.contains(ComplicationType.SHORT_TEXT))
    }
}
