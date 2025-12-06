package com.cosmobond.watchface

import android.content.Context
import android.graphics.RectF
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository

/**
 * Manages the complication slots for the CosmoBond watch face.
 * Defines standard slots: Left, Right, Top, Bottom.
 */
object CompanionComplicationSlots {
    const val LEFT_SLOT_ID = 10
    const val RIGHT_SLOT_ID = 11
    const val TOP_SLOT_ID = 12
    const val BOTTOM_SLOT_ID = 13

    // Standard types supported by most slots
    private val defaultTypes = listOf(
        ComplicationType.SHORT_TEXT,
        ComplicationType.SMALL_IMAGE,
        ComplicationType.MONOCHROMATIC_IMAGE,
        ComplicationType.RANGED_VALUE
    )

    fun create(
        context: Context,
        currentUserStyleRepository: CurrentUserStyleRepository,
    ): ComplicationSlotsManager {
        // Factory to create standard ComplicationDrawables that know how to render themselves
        val drawableFactory = CanvasComplicationFactory { watchState, listener ->
            val drawable = ComplicationDrawable(context)
            CanvasComplicationDrawable(drawable, watchState, listener)
        }

        val leftSlot = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            LEFT_SLOT_ID,
            drawableFactory,
            defaultTypes,
            DefaultComplicationDataSourcePolicy(
                SystemDataSources.DATA_SOURCE_DAY_OF_WEEK,
                ComplicationType.SHORT_TEXT
            ),
            ComplicationSlotBounds(RectF(0.15f, 0.45f, 0.35f, 0.65f))
        )
            .setNameResourceId(R.string.complication_left_name)
            .setScreenReaderNameResourceId(R.string.complication_left_name)
            .build()

        val rightSlot = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            RIGHT_SLOT_ID,
            drawableFactory,
            defaultTypes,
            DefaultComplicationDataSourcePolicy(
                SystemDataSources.DATA_SOURCE_STEP_COUNT,
                ComplicationType.SHORT_TEXT
            ),
            ComplicationSlotBounds(RectF(0.65f, 0.45f, 0.85f, 0.65f))
        )
            .setNameResourceId(R.string.complication_right_name)
            .setScreenReaderNameResourceId(R.string.complication_right_name)
            .build()

        val topSlot = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            TOP_SLOT_ID,
            drawableFactory,
            defaultTypes,
            DefaultComplicationDataSourcePolicy(
                SystemDataSources.DATA_SOURCE_WATCH_BATTERY,
                ComplicationType.RANGED_VALUE
            ),
            ComplicationSlotBounds(RectF(0.4f, 0.1f, 0.6f, 0.3f))
        )
            .setNameResourceId(R.string.complication_top_name)
            .setScreenReaderNameResourceId(R.string.complication_top_name)
            .build()

        val bottomSlot = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            BOTTOM_SLOT_ID,
            drawableFactory,
            defaultTypes,
            DefaultComplicationDataSourcePolicy(
                SystemDataSources.DATA_SOURCE_DATE,
                ComplicationType.SHORT_TEXT
            ),
            ComplicationSlotBounds(RectF(0.4f, 0.7f, 0.6f, 0.9f))
        )
            .setNameResourceId(R.string.complication_bottom_name)
            .setScreenReaderNameResourceId(R.string.complication_bottom_name)
            .build()

        return ComplicationSlotsManager(
            listOf(leftSlot, rightSlot, topSlot, bottomSlot),
            currentUserStyleRepository
        )
    }

    fun normalizedBounds(): List<RectF> =
        listOf(
            RectF(0.15f, 0.45f, 0.35f, 0.65f),
            RectF(0.65f, 0.45f, 0.85f, 0.65f),
            RectF(0.4f, 0.1f, 0.6f, 0.3f),
            RectF(0.4f, 0.7f, 0.6f, 0.9f),
        )
}
