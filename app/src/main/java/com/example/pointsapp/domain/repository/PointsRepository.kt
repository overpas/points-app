package com.example.pointsapp.domain.repository

import com.example.pointsapp.domain.model.Point

interface PointsRepository {

    suspend fun get(count: Int): Result<List<Point>>
}
