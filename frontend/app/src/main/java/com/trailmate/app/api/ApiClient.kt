package com.trailmate.app.api

import android.os.Build
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // Detect emulator vs real device
    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.MODEL.contains("Emulator")
                || Build.PRODUCT.contains("sdk"))
    }

    // Base URL selector for YOUR BACKEND
    private fun getBaseUrl(): String {
        return if (isEmulator())
            "http://10.0.2.2:8000/api/"
        else
            "http://127.0.0.1:8000/api/" // ← usb debugging must ip
    }

    // Logging interceptor
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // HTTP client
    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // ================= BACKEND RETROFIT =================

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // ================= WEATHER RETROFIT =================

    private val weatherRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // ================= SERVICES =================

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val equipmentApi: EquipmentApiService by lazy {
        retrofit.create(EquipmentApiService::class.java)
    }

    val caloriesApi: CaloriesApiService by lazy {
        retrofit.create(CaloriesApiService::class.java)
    }

    val userApiService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
    val userApi = userApiService

    val statsApi: StatsApiService by lazy {
        retrofit.create(StatsApiService::class.java)
    }

    // ✅ WEATHER SERVICE (FIXED)
    val weatherApi: WeatherApiService by lazy {
        weatherRetrofit.create(WeatherApiService::class.java)
    }

}
