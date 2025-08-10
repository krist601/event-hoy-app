package org.jkc.event.tracker.data.util

import kotlinx.datetime.*

fun parseToLocalDateTime(dateStr: String?): LocalDateTime? {
    if (dateStr.isNullOrBlank()) return null
    return try {
        Instant.parse(dateStr).toLocalDateTime(TimeZone.UTC)
    } catch (e: Exception) {
        null
    }
}