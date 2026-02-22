package com.trailmate.app.ui.map

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class RouteStats(
    val distanceMeters: Float = 0f,
    val steps: Int = 0,
    val pedals: Int = 0,
    val minutes: Int = 0
)

class TrackingViewModel(app: Application) : AndroidViewModel(app) {

    private val fusedClient =
        LocationServices.getFusedLocationProviderClient(app)

    // ---------------- PATH ----------------
    private val _path = MutableStateFlow<List<Location>>(emptyList())
    val path: StateFlow<List<Location>> = _path

    // ---------------- DISTANCE ----------------
    private val _distance = MutableStateFlow(0.0)
    val distance: StateFlow<Double> = _distance

    // ---------------- TIMER ----------------
    private val _time = MutableStateFlow(0L)
    val time: StateFlow<Long> = _time

    // ---------------- ROUTE STATS ----------------
    private val _routeStats = MutableStateFlow(RouteStats())
    val routeStats: StateFlow<RouteStats> = _routeStats

    private var lastLocation: Location? = null

    private val request = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        2000
    ).build()

    // =====================================================
    // START TRACKING
    // =====================================================
    @SuppressLint("MissingPermission")
    fun startTracking() {

        fusedClient.requestLocationUpdates(
            request,
            callback,
            null
        )

        startTimer()
    }

    // =====================================================
    // STOP TRACKING
    // =====================================================
    fun stopTracking() {
        fusedClient.removeLocationUpdates(callback)
    }

    // =====================================================
    // LOCATION CALLBACK
    // =====================================================
    private val callback = object : LocationCallback() {

        override fun onLocationResult(result: LocationResult) {

            val loc = result.lastLocation ?: return

            viewModelScope.launch {

                lastLocation?.let {
                    _distance.value += it.distanceTo(loc)
                }

                lastLocation = loc
                _path.value = _path.value + loc
            }
        }
    }

    // =====================================================
    // TIMER
    // =====================================================
    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _time.value += 1
            }
        }
    }

    // =====================================================
    // ROUTE STATS CALCULATION
    // =====================================================
    fun calculateRouteStats(
        startLat: Double,
        startLng: Double,
        destLat: Double,
        destLng: Double,
        mode: String = "walk"
    ) {

        val results = FloatArray(1)

        Location.distanceBetween(
            startLat,
            startLng,
            destLat,
            destLng,
            results
        )

        val distance = results[0]

        val steps = (distance / 0.75f).roundToInt()
        val pedals = (distance / 2.1f).roundToInt()

        val speed = if (mode == "cycle") 15f else 5f
        val minutes = ((distance / 1000f) / speed * 60f).roundToInt()

        _routeStats.value =
            RouteStats(distance, steps, pedals, minutes)
    }
}
