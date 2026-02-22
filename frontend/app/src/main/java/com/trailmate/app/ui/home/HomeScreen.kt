package com.trailmate.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.trailmate.app.models.Route
import com.trailmate.app.ui.calories.CaloriesViewModel
import com.trailmate.app.ui.components.CaloriesStatsCard
import com.trailmate.app.ui.components.ProgressChart
import com.trailmate.app.ui.map.MapScreen
import com.trailmate.app.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLogout: () -> Unit,
    onNavigateToEquipment: () -> Unit,
    onNavigateToCalories: () -> Unit,
    onNavigateToCaloriesHistory: () -> Unit,
    onNavigateToCaloriesStats: () -> Unit,
    token: String = "",
    userId: Int = 0,
    startTab: String
) {

    var selectedTab by rememberSaveable {
        mutableStateOf(
            when (startTab) {
                "activity" -> 2
                "map" -> 1
                else -> 0
            }
        )
    }

    var routes by remember { mutableStateOf(listOf<Route>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (selectedTab) {
                            0 -> "TrailMate"
                            1 -> "Live Map"
                            2 -> "Activity"
                            else -> "Profile"
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White
                )
            )
        },

        bottomBar = {
            NavigationBar(containerColor = PrimaryGreen) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Map, null) },
                    label = { Text("Map") }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.AutoMirrored.Filled.DirectionsRun, null) },
                    label = { Text("Activity") }
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.AccountCircle, null) },
                    label = { Text("You") }
                )
            }
        }
    ) { padding ->

        when (selectedTab) {

            0 -> HomeContent(
                modifier = Modifier.padding(padding),
                routes = routes,
                userId = userId,
                navController = navController
            )

            1 -> MapScreen()

            2 -> ActivityTab(navController)

            3 -> ProfileTab(
                modifier = Modifier.padding(padding),
                onLogout = onLogout
            )
        }
    }
}

//////////////////////////////////////////////////////////////////
// HOME TAB CONTENT
//////////////////////////////////////////////////////////////////

@Composable
private fun HomeContent(
    modifier: Modifier,
    routes: List<Route>,
    userId: Int,
    navController: NavController
) {

    val statsVM: StatsViewModel = viewModel()
    val weatherVM: WeatherViewModel = viewModel()
    val caloriesVM: CaloriesViewModel = viewModel()

    LaunchedEffect(Unit) {
        statsVM.load(userId)
        weatherVM.loadWeather(19.0760, 72.8777)
        caloriesVM.loadHistory(userId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        /// WELCOME CARD
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
                    "Welcome to TrailMate",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    "Ready for your next adventure?",
                    color = Color.White.copy(.85f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        /// WEATHER CARD
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {

                when {
                    weatherVM.loading -> CircularProgressIndicator()

                    weatherVM.temperature != null -> Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Current Weather")

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "${weatherVM.temperature}°C",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Text(
                            "Wind ${weatherVM.windSpeed} km/h",
                            color = Color.Gray
                        )
                    }

                    else -> Text("Weather unavailable")
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        /// DASHBOARD TITLE
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        /// CHART
        ProgressChart(
            weekData = statsVM.stats.map { it.distance }
        )

        Spacer(Modifier.height(18.dp))

        /// CALORIE STATS CARD
        CaloriesStatsCard(
            total = caloriesVM.totalCalories.toInt(),
            average = caloriesVM.avgCalories.toInt(),
            onClick = {
                navController.navigate("calories_stats")
            }
        )
    }
}
