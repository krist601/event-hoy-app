package org.jkc.event.tracker.domain.usecase

import org.jkc.event.tracker.data.repository.EventHoyRepository
import org.jkc.event.tracker.domain.entity.EventEntity

class EventDetailUseCase(
    private val eventHoyRepository: EventHoyRepository
) {
    suspend fun getEventDetail(id: Int): EventEntity {
        return eventHoyRepository.getEventById(id)
    }
}