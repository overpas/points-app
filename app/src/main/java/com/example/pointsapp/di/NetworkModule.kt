package com.example.pointsapp.di

import com.example.pointsapp.BuildConfig
import com.example.pointsapp.data.api.PointsApi
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun loggingInterceptor(): Interceptor =
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun okHttpClient(
        loggingInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(30))
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()

    @Provides
    @Singleton
    fun jsonConverterFactory(): Converter.Factory =
        Json.asConverterFactory("application/json; charset=UTF8".toMediaType())

    @Provides
    @Singleton
    fun resultAdapterFactory(): CallAdapter.Factory =
        ResultCallAdapterFactory.create()

    @Provides
    @Singleton
    fun retrofit(
        okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory,
        resultCallAdapterFactory: CallAdapter.Factory,
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(jsonConverterFactory)
            .addCallAdapterFactory(resultCallAdapterFactory)
            .build()

    @Provides
    @Singleton
    fun pointsApi(retrofit: Retrofit): PointsApi =
        retrofit.create()
}
