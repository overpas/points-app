package com.example.pointsapp.domain

import com.example.pointsapp.domain.model.Point
import com.example.pointsapp.domain.repository.PointsRepository
import javax.inject.Inject

class GetPoints @Inject constructor(
    private val pointsRepository: PointsRepository,
) {

    suspend operator fun invoke(count: Int): Result<List<Point>> =
        pointsRepository.get(count)
}
