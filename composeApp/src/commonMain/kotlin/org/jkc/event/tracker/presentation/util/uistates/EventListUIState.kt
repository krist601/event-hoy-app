package org.jkc.event.tracker.presentation.util.uistates

import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.domain.entity.SubCategoryEntity

data class EventListUIState(
    val eventList: List<EventEntity>,
    val subCategories: List<SubCategoryEntity> = emptyList(),
    val isLoadingMore: Boolean = false
)