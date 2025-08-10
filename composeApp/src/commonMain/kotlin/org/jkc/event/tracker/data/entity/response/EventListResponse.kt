package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable
import org.jkc.event.tracker.data.util.parseToLocalDateTime
import org.jkc.event.tracker.domain.entity.EventEntity

@Serializable
data class EventListResponse(
    val data: List<Event> = emptyList(),
    val pagination: Pagination
) {
    @Serializable
    data class Event(
        val id: Int,
        val title: String? = null,
        val description: String? = null,
        val status: String? = null,
        val ticketPrice: String? = null,
        val imageUrl: String? = null,
        val createdAt: String? = null,
        val updatedAt: String? = null,
        val venue: Venue? = null,
        val category: Category? = null,
        val availableDates: List<AvailableDates>? = null,
        val totalDates: Int? = null,
        val nextDate: String? = null,
        val recurrenceInfo: RecurrenceInfo? = null
    ) {
        @Serializable
        data class Category(
            val id: Int,
            val name: String
        )

        @Serializable
        data class Venue(
            val id: Int,
            val name: String,
            val address: String,
            val city: City
        ) {
            @Serializable
            data class City(
                val id: Int,
                val name: String,
                val country: Country
            ) {
                @Serializable
                data class Country(
                    val id: Int,
                    val name: String
                )
            }
        }
    }

    @Serializable
    data class Pagination(
        val page: Int,
        val limit: Int,
        val total: Int,
        val totalPages: Int,
        val hasNext: Boolean,
        val hasPrev: Boolean
    )

    @Serializable
    data class AvailableDates(
        val id: Int,
        val startDate: String,
        val endDate: String,
    )

    @Serializable
    data class RecurrenceInfo(
        val recurrenceType: String,
        val interval: Int,
        val startDate: String,
        val endDate: String,
    )
}

fun EventListResponse.Event.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        description = description,
        status = status,
        ticketPrice = ticketPrice,
        imageUrl = imageUrl,
        createdAt = parseToLocalDateTime(createdAt.orEmpty()),
        updatedAt = parseToLocalDateTime(updatedAt.orEmpty()),
        venue = venue?.let{
            EventEntity.VenueEntity(
                id = it.id,
                name = it.name,
                address = it.address,
                city = EventEntity.VenueEntity.CityEntity(
                    id = it.city.id,
                    name = it.city.name,
                    country = EventEntity.VenueEntity.CityEntity.CountryEntity(
                        id = it.city.country.id,
                        name = it.city.country.name
                    )
                )
            )
        },
        category = category?.let {
            EventEntity.CategoryEntity(
                id = category.id,
                name = category.name
            )
        },
        availableDates = availableDates?.let {
            availableDates.map {
                EventEntity.AvailableDatesEntity(
                    id = it.id,
                    startDate = it.startDate,
                    endDate = it.endDate,
                )
            } },
        totalDates = totalDates,
        nextDate = nextDate,
        recurrenceInfo = recurrenceInfo?.let {
            EventEntity.RecurrenceInfoEntity(
                recurrenceType = recurrenceInfo.recurrenceType,
                interval = recurrenceInfo.interval,
                startDate = recurrenceInfo.startDate,
                endDate = recurrenceInfo.endDate,
            )
        }
    )
}

fun List<EventListResponse.Event>.toEntity(): List<EventEntity> {
    return this.map { it.toEntity() }
}
