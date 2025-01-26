package com.example.pointsapp.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val count = MutableStateFlow<Int?>(null)

    val letsGoEnabled: StateFlow<Boolean> = count.map { it != null && it > 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val events: SharedFlow<MainEvent>
        field = MutableSharedFlow<MainEvent>()

    fun onCountChanged(count: Int?) {
        this.count.value = count
    }

    fun onLetsGoClicked() {
        viewModelScope.launch {
            val count = this@MainViewModel.count.value
            if (count != null) {
                events.emit(MainEvent.NavigateToPoints(count))
            }
        }
    }
}
