package com.cosmobond.watchface

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal object CompanionTimeFormatter {
    fun formatTime(
        time: ZonedDateTime,
        showSeconds: Boolean,
        use24Hour: Boolean,
    ): String {
        val pattern =
            when {
                use24Hour && showSeconds -> "HH:mm:ss"
                use24Hour && !showSeconds -> "HH:mm"
                !use24Hour && showSeconds -> "hh:mm:ss a"
                else -> "hh:mm a"
            }
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return formatter.format(time)
    }
}
