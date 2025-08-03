package org.jkc.event.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jkc.event.tracker.di.appModule
import org.jkc.event.tracker.expected.interfaces.IExpectedShare
import org.jkc.event.tracker.presentation.ui.eventdetail.EventDetailRoute
import org.jkc.event.tracker.presentation.ui.eventlist.EventListRoute
import org.jkc.event.tracker.presentation.ui.home.HomeRoute
import org.jkc.event.tracker.presentation.ui.navigation.MainNavigationDestination
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.dsl.KoinAppDeclaration

@Composable
@Preview
fun App(koinAppDeclaration: KoinAppDeclaration? = null) {
    KoinApplication(application = {
        koinAppDeclaration?.invoke(this)
        modules(appModule)
    }) {
        MaterialTheme {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .background(Color.White)
            ) {
                MainNavigationHost()
            }
        }
    }
}

@Composable
private fun MainNavigationHost() {
    val navController = rememberNavController()
    val iExpectedShare: IExpectedShare = getKoin().get()

    NavHost(
        navController,
        startDestination = MainNavigationDestination.Home,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<MainNavigationDestination.Home> {
            HomeRoute(
                onEventClick = { eventId ->
                    navController.navigate(MainNavigationDestination.EventDetail(eventId))
                },
                onCategoryEventsClick = { categoryId ->
                    navController.navigate(MainNavigationDestination.EventList(eventFilter = "CategoryEvents", categoryId))
                },
                onUpcomingEventsClick = {
                    navController.navigate(MainNavigationDestination.EventList(eventFilter = "UPCOMING"))
                },
                onSuggestedEventsClick = {
                    navController.navigate(MainNavigationDestination.EventList(eventFilter = "SUGGESTED"))
                }
            )
        }
        composable<MainNavigationDestination.EventList> {
            val route = it.toRoute<MainNavigationDestination.EventList>()
            EventListRoute(
                eventFilter = route.eventFilter,
                onEventClick = { eventId ->
                    navController.navigate(MainNavigationDestination.EventDetail(eventId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<MainNavigationDestination.EventDetail> {
            val route = it.toRoute<MainNavigationDestination.EventDetail>()
            EventDetailRoute(
                eventId = route.eventId,
                onShareClick = { id ->
                    iExpectedShare.shareURL(id)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
