package com.trailmate.app.ui.map

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.camera.CameraUpdateFactory

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    latitude: Double = 28.6139,
    longitude: Double = 77.2090,
    zoomLevel: Double = 12.0
) {

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            try {
                val mapView = MapView(context)
                mapView.onCreate(Bundle())

                mapView.getMapAsync(object : OnMapReadyCallback {
                    override fun onMapReady(map: MapplsMap) {
                        Log.d("MapScreen", "Map is ready")
                        val location = LatLng(latitude, longitude)
                        val cameraUpdate =
                            CameraUpdateFactory.newLatLngZoom(location, zoomLevel)
                        map.animateCamera(cameraUpdate)
                    }

                    override fun onMapError(errorCode: Int, errorMessage: String?) {
                        Log.e("MapScreen", "Map error: $errorCode $errorMessage")
                    }
                })

                mapView
            } catch (e: Exception) {
                Log.e("MapScreen", "Failed to create MapView", e)
                TextView(context).apply {
                    text = "Map failed to load. Check Mappls configuration."
                }
            }
        }
    )
}
