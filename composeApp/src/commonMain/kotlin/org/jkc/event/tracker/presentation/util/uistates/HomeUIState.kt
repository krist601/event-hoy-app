package org.jkc.event.tracker.presentation.util.uistates

import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.domain.entity.LocationEntity
import org.jkc.event.tracker.domain.entity.SubCategoryEntity

data class HomeUIState(
    val upcomingEventList: List<EventEntity>,
    val suggestedEventList: List<EventEntity>,
    val categoryList: List<CategoryEntity>,
    val categorySelected: CategoryEntity?,
    val subCategories: List<SubCategoryEntity>,
    val locationList: List<LocationEntity>,
    val isLoadingMore: Boolean = false
)