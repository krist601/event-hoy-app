package org.jkc.event.tracker.di

import org.jkc.event.tracker.data.datasource.api.APIDataSource
import org.jkc.event.tracker.data.datasource.api.IAPIDataSource
import org.jkc.event.tracker.data.datasource.local.ILocalDataSource
import org.jkc.event.tracker.data.datasource.local.LocalDataSource
import org.jkc.event.tracker.data.repository.EventHoyRepository
import org.jkc.event.tracker.domain.usecase.EventDetailUseCase
import org.jkc.event.tracker.domain.usecase.EventListUseCase
import org.jkc.event.tracker.domain.usecase.HomeUseCase
import org.jkc.event.tracker.expected.classes.ExpectedShare
import org.jkc.event.tracker.expected.interfaces.IExpectedShare
import org.jkc.event.tracker.presentation.ui.eventlist.EventListViewModel
import org.jkc.event.tracker.presentation.ui.eventdetail.EventDetailViewModel
import org.jkc.event.tracker.presentation.ui.home.HomeViewModel
import org.jkc.event.tracker.expected.classes.LocationService
import org.jkc.event.tracker.expected.interfaces.ILocationService
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IAPIDataSource> { APIDataSource() }
    single<ILocalDataSource> { LocalDataSource() }
    single<IExpectedShare> { ExpectedShare() }
    single<EventHoyRepository> {
        EventHoyRepository(
            apiDataSource = get(),
            localDataSource = get()
        )
    }
    single<EventListUseCase> {
        EventListUseCase(
            eventHoyRepository = get()
        )
    }
    single<EventDetailUseCase> {
        EventDetailUseCase(
            eventHoyRepository = get()
        )
    }
    single<HomeUseCase> {
        HomeUseCase(
            eventHoyRepository = get()
        )
    }

    single<ILocationService> { LocationService() }

    viewModel { EventDetailViewModel(eventDetailUseCase = get()) }
    viewModel {
        EventListViewModel(
            eventListUseCase = get()
        )
    }
    viewModel { HomeViewModel(homeUseCase = get(), locationService = get()) }
}

//expect val platformModule: Module