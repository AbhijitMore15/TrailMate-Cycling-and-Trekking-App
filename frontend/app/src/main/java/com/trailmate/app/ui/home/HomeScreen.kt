package com.trailmate.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trailmate.app.models.Route
import com.trailmate.app.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToMap: () -> Unit,
    onLogout: () -> Unit,
    token: String = "",
    userId: Int = 0
) {
    var routes by remember { mutableStateOf(listOf<Route>()) }
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TrailMate-Cycling and Trekking") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = PrimaryGreen
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = PrimaryGreen
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                    label = { Text("Map") },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onNavigateToMap()
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = PrimaryGreen
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DirectionsRun, contentDescription = "Activity") },
                    label = { Text("Activity") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = PrimaryGreen
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = PrimaryGreen
                    )
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> HomeContent(modifier = Modifier.padding(padding), routes = routes)
            1 -> {} // Map navigation handled by parent
            2 -> ActivityContent(modifier = Modifier.padding(padding))
            3 -> SettingsContent(modifier = Modifier.padding(padding), onLogout = onLogout)
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    routes: List<Route>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Welcome Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(12.dp),
            color = PrimaryGreen
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Welcome back!",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Ready for your next adventure?",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard("Total Distance", "0 km", modifier = Modifier.weight(1f))
            StatCard("Activities", "0", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recommended Routes
        Text(
            "Recommended Routes",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (routes.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("No routes available", color = Color.Gray)
                }
            }
        } else {
            routes.take(3).forEach { route ->
                RouteCard(route)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun RouteCard(route: Route) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(route.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    "${route.distance} km • ${route.elevation}m elevation",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Icon(Icons.Default.ArrowForward, contentDescription = "View route")
        }
    }
}

@Composable
private fun ActivityContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA)),
        contentAlignment = Alignment.Center
    ) {
        Text("Activity history coming soon...", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC62828)
            )
        ) {
            Text("Logout")
        }
    }
}