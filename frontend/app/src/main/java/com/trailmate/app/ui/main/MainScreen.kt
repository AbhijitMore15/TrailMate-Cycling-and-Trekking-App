package com.trailmate.app.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (selectedTab) {
                            0 -> "Home"
                            1 -> "Live Map"
                            2 -> "Activity"
                            else -> "Profile"
                        }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Text("🏠") },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Text("🗺") },
                    label = { Text("Map") }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Text("🏃") },
                    label = { Text("Activity") }
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Text("👤") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->

        when (selectedTab) {

            0 -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text("Home Screen")
            }

            1 -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SimpleMapScreen()
            }

            2 -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text("Activity Screen")
            }

            3 -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text("Profile Screen")
            }
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SimpleMapScreen() {

    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->

            // Required OSM Configuration
            Configuration.getInstance().load(
                ctx,
                ctx.getSharedPreferences("osm", 0)
            )
            Configuration.getInstance().userAgentValue = ctx.packageName

            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
            }
        }
    )
}
