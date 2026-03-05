package com.trailmate.app.ui.map

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import java.net.URL
import java.util.Locale
import kotlin.math.roundToInt

import com.trailmate.app.repository.ActivityRepository
import com.trailmate.app.models.Activity

@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {

    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()

    var lastLocation by remember { mutableStateOf<Location?>(null) }
    var bearing by remember { mutableFloatStateOf(0f) }
    var searchText by remember { mutableStateOf("") }
    var destinationPoint by remember { mutableStateOf<GeoPoint?>(null) }
    var autoCenter by remember { mutableStateOf(false) }

    /// ACTIVITY STATE (NEW)
    var activityStarted by remember { mutableStateOf(false) }
    var activityMode by remember { mutableStateOf("Cycling") }
    var startTime by remember { mutableLongStateOf(0L) }

    // ---------- ROUTE STATS ----------
    var distanceMeters by remember { mutableFloatStateOf(0f) }
    var steps by remember { mutableIntStateOf(0) }
    var pedals by remember { mutableIntStateOf(0) }
    var minutes by remember { mutableIntStateOf(0) }

    val fusedLocationClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    val mapView = remember { MapView(context) }

    // ---------- MAP SETUP ----------
    LaunchedEffect(Unit) {

        Configuration.getInstance().apply {
            tileFileSystemCacheMaxBytes = 20L * 1024 * 1024
            tileFileSystemCacheTrimBytes = 5L * 1024 * 1024
            userAgentValue = context.packageName
        }

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)
        mapView.minZoomLevel = 4.0
        mapView.maxZoomLevel = 18.0

        val rotation = RotationGestureOverlay(mapView)
        rotation.isEnabled = true
        mapView.overlays.add(rotation)

        mapView.post {
            mapView.controller.setCenter(GeoPoint(20.5937, 78.9629))
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            it?.let { loc ->
                mapView.controller.animateTo(
                    GeoPoint(loc.latitude, loc.longitude)
                )
            }
        }

        mapView.setOnTouchListener { _, _ ->
            autoCenter = false
            false
        }

        mapView.onResume()
    }

    // ---------- MARKERS ----------
    val userMarker = remember {
        Marker(mapView).apply {
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            icon = androidx.core.content.ContextCompat.getDrawable(
                context,
                com.trailmate.app.R.drawable.ic_navigation_arrow
            )
        }
    }

    val destinationMarker = remember {
        Marker(mapView).apply {
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

            val size = 28
            val bmp = android.graphics.Bitmap.createBitmap(
                size, size,
                android.graphics.Bitmap.Config.ARGB_8888
            )

            val canvas = android.graphics.Canvas(bmp)

            val paint = android.graphics.Paint().apply {
                color = Color.RED
                isAntiAlias = true
            }

            canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

            icon = android.graphics.drawable.BitmapDrawable(
                context.resources,
                bmp
            )
        }
    }

    val routeLine = remember { Polyline() }

    // ---------- ROUTE FETCH ----------
    fun fetchRoute(start: GeoPoint, end: GeoPoint) {

        scope.launch {

            val url =
                "https://router.project-osrm.org/route/v1/driving/" +
                        "${start.longitude},${start.latitude};" +
                        "${end.longitude},${end.latitude}" +
                        "?overview=full&geometries=geojson"

            val json = withContext(Dispatchers.IO) {
                try { URL(url).readText() }
                catch (e: Exception) { null }
            } ?: return@launch

            val coords = JSONObject(json)
                .getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONArray("coordinates")

            val points = mutableListOf<GeoPoint>()

            for (i in 0 until coords.length()) {
                val c = coords.getJSONArray(i)
                points.add(GeoPoint(c.getDouble(1), c.getDouble(0)))
            }

            routeLine.setPoints(points)
            routeLine.outlinePaint.color = Color.BLUE
            routeLine.outlinePaint.strokeWidth = 8f

            mapView.invalidate()
        }
    }

    // ---------- SEARCH ----------
    fun searchLocation(query: String) {

        if (query.isBlank()) return

        scope.launch {

            val result = withContext(Dispatchers.IO) {
                try { geocoder.getFromLocationName(query, 1) }
                catch (e: Exception) { null }
            }

            if (!result.isNullOrEmpty()) {

                val point = GeoPoint(result[0].latitude, result[0].longitude)
                destinationPoint = point
                destinationMarker.position = point

                if (!mapView.overlays.contains(destinationMarker))
                    mapView.overlays.add(destinationMarker)

                mapView.controller.animateTo(point)
                mapView.invalidate()
            }
        }
    }

    // ---------- COMPASS ----------
    LaunchedEffect(Unit) {
        while (true) {
            bearing = mapView.mapOrientation
            delay(120)
        }
    }

    // ---------- LOCATION ----------
    DisposableEffect(Unit) {

        mapView.overlays.add(userMarker)
        mapView.overlays.add(routeLine)

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000
        ).build()

        val callback = object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {

                val location = result.lastLocation ?: return
                val geo = GeoPoint(location.latitude, location.longitude)

                lastLocation = location
                userMarker.position = geo
                userMarker.rotation = location.bearing

                destinationPoint?.let { dest ->

                    val results = FloatArray(1)

                    Location.distanceBetween(
                        geo.latitude,
                        geo.longitude,
                        dest.latitude,
                        dest.longitude,
                        results
                    )

                    distanceMeters = results[0]
                    steps = (distanceMeters / 0.75f).roundToInt()
                    pedals = (distanceMeters / 2.1f).roundToInt()
                    minutes = ((distanceMeters / 1000f) / 15f * 60f).roundToInt()

                    fetchRoute(geo, dest)
                }

                if (autoCenter)
                    mapView.controller.setCenter(geo)

                mapView.postInvalidate()
            }
        }

        fusedLocationClient.requestLocationUpdates(
            request,
            callback,
            context.mainLooper
        )

        onDispose {
            fusedLocationClient.removeLocationUpdates(callback)
            mapView.onPause()
            mapView.onDetach()
        }
    }

    // ---------- UI ----------
    Box(Modifier.fillMaxSize()) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView }
        )

        /// SEARCH BAR
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 90.dp)
                .fillMaxWidth(.9f),
            shadowElevation = 8.dp
        ) {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search Location") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { searchLocation(searchText) }) {
                        Text("🔍")
                    }
                }
            )
        }

        /// ACTIVITY CONTROLS (NEW)
        if (destinationPoint != null) {

            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 160.dp)
                    .fillMaxWidth(.9f)
            ) {

                Column(
                    Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text("Mode:")

                        Spacer(Modifier.width(10.dp))

                        FilterChip(
                            selected = activityMode == "Cycling",
                            onClick = { activityMode = "Cycling" },
                            label = { Text("🚴 Cycling") }
                        )

                        Spacer(Modifier.width(8.dp))

                        FilterChip(
                            selected = activityMode == "Trekking",
                            onClick = { activityMode = "Trekking" },
                            label = { Text("🥾 Trekking") }
                        )
                    }

                    Button(
                        onClick = {

                            if (!activityStarted) {

                                activityStarted = true
                                startTime = System.currentTimeMillis()

                            } else {

                                activityStarted = false

                                val duration =
                                    ((System.currentTimeMillis() - startTime) / 60000).toInt()

                                val km = distanceMeters / 1000.0

                                ActivityRepository.addActivity(
                                    Activity(
                                        id = 0,
                                        routeId = 0,
                                        userId = 0,
                                        distance = km,
                                        time = duration,
                                        steps = steps,
                                        pedals = pedals,
                                        durationMin = duration,
                                        type = activityMode,
                                        timestamp = System.currentTimeMillis().toString()
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        if (!activityStarted)
                            Text("Start Activity")
                        else
                            Text("Finish Activity")
                    }
                }
            }
        }

        /// ROUTE STATS
        if (distanceMeters > 0f) {

            val km = distanceMeters / 1000f

            Card(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 120.dp)
            ) {

                Column(Modifier.padding(14.dp)) {

                    Text("Route Stats")

                    Spacer(Modifier.height(6.dp))

                    Text("Steps: $steps")
                    Text("Pedals: $pedals")
                    Text("ETA: $minutes min")
                    Text("Distance: %.2f km".format(km))
                }
            }
        }

        /// CONTROLS
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            FloatingActionButton(onClick = { mapView.mapOrientation = 0f }) {
                Text("⬆️", modifier = Modifier.graphicsLayer { rotationZ = bearing })
            }

            FloatingActionButton(onClick = { mapView.controller.zoomIn() }) {
                Text("+", fontSize = 20.sp)
            }

            FloatingActionButton(onClick = { mapView.controller.zoomOut() }) {
                Text("-", fontSize = 20.sp)
            }

            FloatingActionButton(
                onClick = {
                    lastLocation?.let {
                        autoCenter = true
                        mapView.controller.animateTo(
                            GeoPoint(it.latitude, it.longitude)
                        )
                    }
                }
            ) { Text("\uD83D\uDD34") }
        }
    }
}