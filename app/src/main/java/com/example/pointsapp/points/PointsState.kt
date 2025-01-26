package com.example.pointsapp.points

import com.example.pointsapp.domain.model.Point

sealed class PointsState {

    data object Initial : PointsState()

    data object Loading : PointsState()

    data object Error : PointsState()

    data class Content(
        val points: List<Point>,
    ) : PointsState()
}
