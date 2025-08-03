package org.jkc.event.tracker.presentation.util.uistates

import org.jkc.event.tracker.domain.entity.EventEntity

data class EventListUIState(
    val eventList: List<EventEntity>,
    val isLoadingMore: Boolean = false
)