package org.jkc.event.tracker.presentation.ui.navigation

import kotlinx.serialization.Serializable

sealed interface MainNavigationDestination {
    @Serializable
    data object Home: MainNavigationDestination
    @Serializable
    data class EventList(val eventFilter: String, val categoryId: Int = 0) : MainNavigationDestination
    @Serializable
    data class EventDetail(val eventId: Int): MainNavigationDestination
}