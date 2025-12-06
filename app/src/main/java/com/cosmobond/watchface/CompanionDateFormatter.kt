package com.cosmobond.watchface

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal object CompanionDateFormatter {
    fun formatDate(
        time: ZonedDateTime,
        formatVariant: DateFormatVariant,
    ): String {
        val formatter =
            when (formatVariant) {
                DateFormatVariant.LONG -> DateTimeFormatter.ofPattern("EEE, MMM d")
                DateFormatVariant.NUMERIC -> DateTimeFormatter.ofPattern("MM/dd")
                else -> DateTimeFormatter.ofPattern("MMM d")
            }
        return formatter.format(time)
    }
}
