package com.cosmobond.watchface

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal object CompanionDateFormatter {
    private val formatter = DateTimeFormatter.ofPattern("MMM d")

    fun formatDate(time: ZonedDateTime): String = formatter.format(time)
}
