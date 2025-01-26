package com.example.pointsapp.di

import com.example.pointsapp.data.repository.PointsRepositoryImpl
import com.example.pointsapp.domain.repository.PointsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun pointsRepository(impl: PointsRepositoryImpl): PointsRepository
}
