package org.jkc.event.tracker.data.entity.request

data class CreateEventRequest(
    val title: String,
    val description: String,
    val startDate: String,
    val categoryId: Int,
    val venueId: Int
)