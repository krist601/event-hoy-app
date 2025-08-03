package org.jkc.event.tracker.presentation.ui.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.domain.usecase.EventDetailUseCase
import org.jkc.event.tracker.presentation.ui.common.ErrorType

class EventDetailViewModel(
    private val eventDetailUseCase: EventDetailUseCase
): ViewModel() {
    private val _state = MutableStateFlow<EventViewState>(EventViewState.Loading)
    val state: StateFlow<EventViewState> = _state

    fun fetchEvent(id: Int) {
        viewModelScope.launch {
            _state.value = EventViewState.Loading
            try {
                val event = eventDetailUseCase.getEventDetail(id)
                _state.value = EventViewState.Success(event)
            } catch (_: IOException) {
                _state.value = EventViewState.Error(ErrorType.NoInternet)
            } catch (_: HttpException) {
                _state.value = EventViewState.Error(ErrorType.ServerError)
            } catch (e: Exception) {
                _state.value = EventViewState.Error(ErrorType.Unknown)
            }
        }
    }

}

sealed interface EventViewState {
    data object Loading : EventViewState
    data class Success(val event: EventEntity) : EventViewState
    data class Error(val errorType: ErrorType) : EventViewState
}