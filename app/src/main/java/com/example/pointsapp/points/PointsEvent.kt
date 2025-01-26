package com.example.pointsapp.points

sealed class PointsEvent {

    data object NavigateBack : PointsEvent()
}
