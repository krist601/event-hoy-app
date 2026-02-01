package org.jkc.event.tracker.domain.entity

import kotlinx.datetime.LocalDateTime

data class EventEntity(
    val id: Int,
    val title: String? = null,
    val description: String? = null,
    val status: String? = null,
    val ticketPrice: String? = null,
    val imageUrl: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val venue: VenueEntity? = null,
    val category: CategoryEntity? = null,
    val availableDates: List<AvailableDatesEntity>? = null,
    val totalDates: Int? = null,
    val nextDate: String? = null,
    val recurrenceInfo: RecurrenceInfoEntity? = null
) {
    data class CategoryEntity(
        val id: Int,
        val name: String
    )

    data class VenueEntity(
        val id: Int,
        val name: String,
        val address: String,
        val latitude: Double,
        val longitude: Double,
        val url: String?
    )

    data class AvailableDatesEntity(
        val id: Int,
        val startDate: String,
        val endDate: String,
    )

    data class RecurrenceInfoEntity(
        val recurrenceType: String,
        val interval: Int,
        val startDate: String,
        val endDate: String,
    )
}