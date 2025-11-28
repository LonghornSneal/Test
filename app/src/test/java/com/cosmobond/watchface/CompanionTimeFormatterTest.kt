package com.cosmobond.watchface

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class CompanionTimeFormatterTest {
    @Test
    fun formatsTwentyFourHourClock() {
        val time = ZonedDateTime.of(2025, 5, 1, 6, 5, 0, 0, ZoneId.of("UTC"))
        assertEquals("06:05", CompanionTimeFormatter.formatTime(time))
    }

    @Test
    fun formatsEvening() {
        val time = ZonedDateTime.of(2025, 5, 1, 23, 45, 0, 0, ZoneId.of("UTC"))
        assertEquals("23:45", CompanionTimeFormatter.formatTime(time))
    }
}
