package org.jkc.event.tracker.presentation.util.uistates

import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity

data class HomeUIState(
    val upcomingEventList: List<EventEntity>,
    val suggestedEventList: List<EventEntity>,
    val categoryList: List<CategoryEntity>,
    val isLoadingMore: Boolean = false
)