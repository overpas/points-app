package com.example.pointsapp.data.api.response

import com.example.pointsapp.domain.model.Point
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PointsResponse(
    @SerialName("points")
    val points: List<PointResponse>,
)

@Serializable
data class PointResponse(
    @SerialName("x")
    val x: Double,
    @SerialName("y")
    val y: Double,
)

fun PointsResponse.toPoints(): List<Point> =
    points.map(PointResponse::toPoint)

private fun PointResponse.toPoint(): Point =
    Point(
        x = x,
        y = y,
    )
