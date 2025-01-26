package com.example.pointsapp.points

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointsapp.domain.GetPoints
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val KEY_COUNT = "count"

@HiltViewModel(assistedFactory = PointsViewModel.Factory::class)
class PointsViewModel @AssistedInject constructor(
    @Assisted count: Int,
    private val getPoints: GetPoints,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val count = savedStateHandle.getStateFlow(KEY_COUNT, count)

    private val mutableState = MutableStateFlow<PointsState>(PointsState.Initial)
    val state: StateFlow<PointsState> = mutableState
        .onStart {
            loadPoints()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PointsState.Initial)

    val events: SharedFlow<PointsEvent>
        field = MutableSharedFlow<PointsEvent>()

    private fun loadPoints() {
        viewModelScope.launch {
            mutableState.value = PointsState.Loading
            getPoints(count.value)
                .onSuccess { points ->
                    mutableState.value = PointsState.Content(points)
                }
                .onFailure { error ->
                    mutableState.value = PointsState.Error
                }
        }
    }

    fun onGoBackClicked() {
        viewModelScope.launch {
            events.emit(PointsEvent.NavigateBack)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(count: Int): PointsViewModel
    }
}
