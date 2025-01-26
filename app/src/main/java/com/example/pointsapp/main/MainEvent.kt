package com.example.pointsapp.main

sealed class MainEvent {

    data class NavigateToPoints(
        val count: Int,
    ) : MainEvent()
}
