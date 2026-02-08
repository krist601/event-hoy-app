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
import org.jkc.event.tracker.expected.interfaces.ILocationService

class HomeViewModel(
    private val homeUseCase: HomeUseCase,
    private val locationService: ILocationService
): ViewModel() {
    private val _state = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val state: StateFlow<HomeViewState> = _state
    private var page = 1
    private var hasMorePages = false
    private var memorySuggestedEventList: MutableList<EventEntity> = mutableListOf()
    private var upcomingEventList: List<EventEntity> = emptyList()
    private var categoryList: List<CategoryEntity> = emptyList()
    private var locationList: List<LocationEntity> = emptyList()

    var text: String? = null
    var category: String? = null
    var location: String? = null
    var startDate: LocalDate? = null
    var endDate: LocalDate? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var radius: Int? = null

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
                locationList = homeUseCase.getLocationList()
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
    fun fetchEventList() {
        viewModelScope.launch {
            val isFiltering = !text.isNullOrEmpty() || !category.isNullOrEmpty() || !location.isNullOrEmpty()
            val isFilteringDate = !startDate?.toString().isNullOrEmpty() || !endDate?.toString().isNullOrEmpty()

            _state.value = HomeViewState.Loading
            try {

                val events = homeUseCase.getEventList(
                    text = text.orEmpty(),
                    page = page,
                    category = category.orEmpty(),
                    location = location.orEmpty(),
                    startDate = startDate?.toString().orEmpty(),
                    endDate = endDate?.toString().orEmpty(),
                    latitude = latitude,
                    longitude = longitude,
                    radius = 5//radius
                )
                memorySuggestedEventList.addAll(events.first)
                hasMorePages = events.second
                _state.value = HomeViewState.Success(
                    suggestedEventList = memorySuggestedEventList,
                    upcomingEventList = if (isFiltering || isFilteringDate) emptyList() else upcomingEventList,
                    categoryList = if (isFiltering) emptyList() else categoryList,
                    locationList = locationList,
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
    fun resetFilters(){
        text = null
        category = null
        location = null
        startDate = null
        endDate = null
        latitude = null
        longitude = null
        radius = null
    }
    fun fetchNewPage(){
        if(hasMorePages) {
            this.page += 1
            fetchEventList()
        }
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            val locationEntity = locationService.getCurrentLocation()
            if (locationEntity != null) {
                latitude = locationEntity.latitude
                longitude = locationEntity.longitude
                location = null
                resetPages()
                fetchEventList()
            }
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