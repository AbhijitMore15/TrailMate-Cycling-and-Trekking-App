/**package com.trailmate.app.ui.map

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

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

    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->

            val mapView = MapView(context)
            mapView.onCreate(Bundle())

            lifecycleOwner.lifecycle.addObserver(
                LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_START -> mapView.onStart()
                        Lifecycle.Event.ON_RESUME -> mapView.onResume()
                        Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                        Lifecycle.Event.ON_STOP -> mapView.onStop()
                        Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                        else -> {}
                    }
                }
            )

            mapView.getMapAsync(object : OnMapReadyCallback {

                override fun onMapReady(mapplsMap: MapplsMap) {

                    val location = LatLng(latitude, longitude)
                    val cameraUpdate =
                        CameraUpdateFactory.newLatLngZoom(location, zoomLevel)

                    mapplsMap.animateCamera(cameraUpdate)
                }

                // ⭐ REQUIRED METHOD (You were missing this)
                override fun onMapError(code: Int, message: String?) {
                    Log.e("Mappls", "Map Error: $code - $message")
                }
            })

            mapView
        }
    )
}**/
