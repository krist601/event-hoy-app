package org.jkc.event.tracker.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.io.IOException
import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.domain.entity.LocationEntity
import org.jkc.event.tracker.domain.usecase.HomeUseCase
import org.jkc.event.tracker.presentation.ui.common.ErrorType
import org.jkc.event.tracker.presentation.util.extensions.simpleDateFormat

class HomeViewModel(
    private val homeUseCase: HomeUseCase
): ViewModel() {
    private val _state = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val state: StateFlow<HomeViewState> = _state
    private var page = 1
    private var hasMorePages = false
    private var memorySuggestedEventList: MutableList<EventEntity> = mutableListOf()
    private var upcomingEventList: List<EventEntity> = emptyList()
    private var categoryList: List<CategoryEntity> = emptyList()

    init {
        fetchHomeData()
    }

    private fun fetchHomeData(
        text: String? = null
    ) {
        viewModelScope.launch {
            _state.value = HomeViewState.Loading
            try {
                upcomingEventList = homeUseCase.getEventListType(
                    text = text,
                    type = EventType.SuggestedEvent.type,
                )
                val suggestedEventList = homeUseCase.getEventList(
                    text = text,
                    type = EventType.UpcomingEvent.type,
                )
                val locationList = homeUseCase.getLocationList()
                memorySuggestedEventList = suggestedEventList.first.toMutableList()
                categoryList = homeUseCase.getCategoryList()
                hasMorePages = suggestedEventList.second
                _state.value = HomeViewState.Success(memorySuggestedEventList, upcomingEventList, categoryList, locationList, false)
            } catch (_: IOException) {
                _state.value = HomeViewState.Error(ErrorType.NoInternet)
            } catch (_: HttpException) {
                _state.value = HomeViewState.Error(ErrorType.ServerError)
            } catch (e: Exception) {
                _state.value = HomeViewState.Error(ErrorType.Unknown)
            }
        }
    }
    fun fetchEventList(
        text: String? = null,
        category: String? = null,
        location: String? = null,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Int? = null
    ) {
        viewModelScope.launch {
            _state.value = HomeViewState.Success(
                suggestedEventList = memorySuggestedEventList,
                isLoadingMore = true
            )
            try {

                val events = homeUseCase.getEventList(
                    text = text,
                    page = page,
                    category = category,
                    location = location,
                    startDate = startDate.toString(),
                    endDate = endDate.toString(),
                    latitude = latitude,
                    longitude = longitude,
                    radius = 5//radius
                )
                memorySuggestedEventList.addAll(events.first)
                hasMorePages = events.second
                _state.value = HomeViewState.Success(
                    suggestedEventList = memorySuggestedEventList,
                    upcomingEventList = upcomingEventList,
                    categoryList = categoryList,
                    isLoadingMore = false
                )
            } catch (_: IOException) {
                _state.value = HomeViewState.Error(ErrorType.NoInternet)
            } catch (_: HttpException) {
                _state.value = HomeViewState.Error(ErrorType.ServerError)
            } catch (e: Exception) {
                _state.value = HomeViewState.Error(ErrorType.Unknown)
            }
        }
    }
    fun resetPages(){
        this.page = 1
        memorySuggestedEventList = mutableListOf()
    }
    fun fetchNewPage(){
        if(hasMorePages) {
            this.page += 1
            fetchEventList()
        }
    }
}

sealed interface HomeViewState {
    data object Loading: HomeViewState
    data class Success(
        val upcomingEventList: List<EventEntity> = emptyList(),
        val suggestedEventList: List<EventEntity> = emptyList(),
        val categoryList: List<CategoryEntity> = emptyList(),
        val locationList: List<LocationEntity> = emptyList(),
        val isLoadingMore: Boolean = false
    ) : HomeViewState
    data class Error(val errorType: ErrorType) : HomeViewState
}

sealed class EventType(val type: String){
    object AllEvent: EventType(type = "ALL")
    object UpcomingEvent: EventType(type = "UPCOMING")
    object SuggestedEvent: EventType(type = "SUGGESTED")
}