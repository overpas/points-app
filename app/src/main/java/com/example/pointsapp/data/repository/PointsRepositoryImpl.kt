package com.example.pointsapp.data.repository

import com.example.pointsapp.data.api.PointsApi
import com.example.pointsapp.data.api.response.PointsResponse
import com.example.pointsapp.data.api.response.toPoints
import com.example.pointsapp.domain.model.Point
import com.example.pointsapp.domain.repository.PointsRepository
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(
    private val pointsApi: PointsApi,
) : PointsRepository {

    override suspend fun get(count: Int): Result<List<Point>> =
        pointsApi.getPoints(count)
            .map(PointsResponse::toPoints)
}
