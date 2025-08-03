package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable
import org.jkc.event.tracker.domain.entity.EventEntity

@Serializable
data class EventListResponse(
    val events: List<Event> = emptyList(),
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val currentPage: Int = 0
) {

    @Serializable
    data class Event(
        val id: Int,
        val title: String,
        val description: String,
        val startDate: String,
        val endDate: String?,
        val image: String,
        val externalUrl: String?,
        val source: String?,
        val priceFrom: String?,
        val featured: Boolean,
        val ticketSaleStart: String?,
        val ticketSaleEnd: String?,
        val status: String,
        val categoryId: Int,
        val venueId: Int,
        val createdAt: String,
        val updatedAt: String,
        val category: Category,
        val venue: Venue
    ) {
        @Serializable
        data class Category(
            val id: Int,
            val name: String,
            val icon: String,
            val position: Int,
            val status: String,
            val createdAt: String,
            val updatedAt: String
        )

        @Serializable
        data class Venue(
            val id: Int,
            val name: String,
            val address: String,
            val latitude: Double,
            val longitude: Double,
            val type: String,
            val websiteUrl: String,
            val image: String,
            val status: String,
            val cityId: Int,
            val createdAt: String,
            val updatedAt: String,
            val city: City
        ) {
            @Serializable
            data class City(
                val id: Int,
                val name: String,
                val image: String,
                val countryId: Int,
                val createdAt: String,
                val updatedAt: String
            )
        }
    }
}

fun EventListResponse.Event.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        image = image,
        externalUrl = externalUrl,
        source = source,
        priceFrom = priceFrom,
        featured = featured,
        ticketSaleStart = ticketSaleStart,
        ticketSaleEnd = ticketSaleEnd,
        status = status,
        categoryId = categoryId,
        venueId = venueId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<EventListResponse.Event>.toEntity(): List<EventEntity> {
    return this.map { it.toEntity() }
}
