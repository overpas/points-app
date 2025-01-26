package com.example.pointsapp.data.api

import com.example.pointsapp.data.api.response.PointsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PointsApi {

    @GET("api/test/points")
    suspend fun getPoints(@Query("count") count: Int): Result<PointsResponse>
}
