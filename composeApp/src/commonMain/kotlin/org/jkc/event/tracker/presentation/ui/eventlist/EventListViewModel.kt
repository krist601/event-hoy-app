package org.jkc.event.tracker.presentation.ui.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.domain.entity.SubCategoryEntity
import org.jkc.event.tracker.domain.usecase.EventListUseCase
import org.jkc.event.tracker.presentation.ui.common.ErrorType

class EventListViewModel(
    private val eventListUseCase: EventListUseCase
): ViewModel() {
    private val _state = MutableStateFlow<EventListViewState>(EventListViewState.Loading)
    val state: StateFlow<EventListViewState> = _state
    private val _subCategories = MutableStateFlow<List<SubCategoryEntity>>(emptyList())
    val subCategories: StateFlow<List<SubCategoryEntity>> = _subCategories

    private var type: String? = null
    private var category: String? = null
    private var page = 1
    private var hasMorePages = false
    private var memoryEventList: MutableList<EventEntity> = mutableListOf()

    fun fetchEventList(
        text: String? = null
    ) {
        viewModelScope.launch {
            if(page == 1)
                _state.value = EventListViewState.Loading
            else {
                _state.value = EventListViewState.Success(
                    eventList = memoryEventList,
                    isLoadingMore = true
                )
            }
            try {
                _subCategories.value = eventListUseCase.getSubCategories(category)
                val events = eventListUseCase.getEventList(
                    text = text,
                    type = type,
                    page = page,
                    category = category
                )
                memoryEventList.addAll(events.first)
                hasMorePages = events.second
                _state.value = EventListViewState.Success(
                    memoryEventList,
                    isLoadingMore = false
                )
            } catch (_: IOException) {
                _state.value = EventListViewState.Error(ErrorType.NoInternet)
            } catch (_: HttpException) {
                _state.value = EventListViewState.Error(ErrorType.ServerError)
            } catch (e: Exception) {
                _state.value = EventListViewState.Error(ErrorType.Unknown)
            }
        }
    }
    fun setEventType(type: String){
        this.type = type
    }
    fun setCategory(category: Int?){
        this.category = category.toString()
    }
    fun fetchNewPage(){
        if(hasMorePages) {
            this.page += 1
            fetchEventList()
        }
    }
    fun resetPages(){
        this.page = 1
        memoryEventList = mutableListOf()
    }
}

sealed interface EventListViewState {
    data object Loading : EventListViewState
    data class Success(
        val eventList: List<EventEntity> = emptyList(),
        val isLoadingMore: Boolean = false
    ) : EventListViewState
    data class Error(val errorType: ErrorType) : EventListViewState
}