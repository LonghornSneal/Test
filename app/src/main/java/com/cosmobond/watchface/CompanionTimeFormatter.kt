package com.cosmobond.watchface

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal object CompanionTimeFormatter {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    fun formatTime(time: ZonedDateTime): String = formatter.format(time)
}
