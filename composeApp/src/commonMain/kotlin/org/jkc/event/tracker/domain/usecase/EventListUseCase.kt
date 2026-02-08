package org.jkc.event.tracker.domain.usecase

import org.jkc.event.tracker.data.repository.EventHoyRepository
import org.jkc.event.tracker.domain.entity.EventEntity

class EventListUseCase(
    private val eventHoyRepository: EventHoyRepository
) {
    suspend fun getEventList(
        text: String? = null,
        type: String? = null,
        page: Int? = null,
        category: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Int? = null
    ): Pair<List<EventEntity>, Boolean> {
        return eventHoyRepository.getEventList(
            text = text,
            type = type,
            page = page,
            category = category,
            startDate = startDate,
            endDate = endDate,
            latitude = latitude,
            longitude = longitude,
            radius = radius
        )
    }
}