package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateEventResponse(
    val featured: Boolean,
    val id: Int,
    val title: String,
    val description: String,
    val startDate: String,
    val categoryId: Int,
    val venueId: Int,
    val status: String,
    val updatedAt: String,
    val createdAt: String
)