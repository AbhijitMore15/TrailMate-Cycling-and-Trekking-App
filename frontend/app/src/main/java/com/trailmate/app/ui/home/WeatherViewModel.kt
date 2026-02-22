package com.trailmate.app.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmate.app.api.ApiClient
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var temperature by mutableStateOf<Double?>(null)
    var windSpeed by mutableStateOf<Double?>(null)
    var loading by mutableStateOf(true)

    fun loadWeather(lat: Double, lon: Double) {

        viewModelScope.launch {
            try {
                val res = ApiClient.weatherApi.getWeather(lat, lon)

                temperature = res.current_weather.temperature
                windSpeed = res.current_weather.windspeed

            } catch (e: Exception) {
                e.printStackTrace()
            }

            loading = false
        }
    }
}
