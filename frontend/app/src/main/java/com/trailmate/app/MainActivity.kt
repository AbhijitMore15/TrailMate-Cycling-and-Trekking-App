package com.trailmate.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trailmate.app.ui.calories.*
import com.trailmate.app.ui.equipment.EquipmentScreen
import com.trailmate.app.ui.home.HomeScreen
import com.trailmate.app.ui.login.LoginScreen
import com.trailmate.app.ui.login.RegisterScreen
import com.trailmate.app.ui.map.MapScreen
import com.trailmate.app.ui.theme.TrailMateTheme
import com.trailmate.app.utils.DataStoreManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager = DataStoreManager(this)

        /// LOCATION PERMISSION
        val permissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        /// UI
        setContent {
            TrailMateTheme {

                val navController = rememberNavController()
                var userIdState by remember { mutableStateOf<Int?>(null) }

                /// LOAD USER ID
                LaunchedEffect(Unit) {
                    dataStoreManager.userIdFlow.collect { id ->
                        userIdState = id
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {

                    /// LOGIN
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { token, userId ->

                                lifecycleScope.launch {
                                    dataStoreManager.saveAuthToken(token)
                                    dataStoreManager.saveUserId(userId)
                                }

                                userIdState = userId

                                navController.navigate("home?tab=home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                navController.navigate("register")
                            }
                        )
                    }

                    /// REGISTER
                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = { token, userId ->

                                lifecycleScope.launch {
                                    dataStoreManager.saveAuthToken(token)
                                    dataStoreManager.saveUserId(userId)
                                }

                                userIdState = userId

                                navController.navigate("home?tab=home") {
                                    popUpTo("register") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }

                    /// HOME
                    composable("home?tab={tab}") { backStackEntry ->

                        val tab =
                            backStackEntry.arguments?.getString("tab") ?: "home"

                        userIdState?.let { uid ->
                            HomeScreen(
                                navController = navController,
                                userId = uid,
                                token = "",
                                startTab = tab,

                                onLogout = {
                                    lifecycleScope.launch {
                                        dataStoreManager.clearAllData()
                                        navController.navigate("login") {
                                            popUpTo("home?tab={tab}") { inclusive = true }
                                        }
                                    }
                                },

                                onNavigateToEquipment = {
                                    navController.navigate("equipment")
                                },

                                onNavigateToCalories = {
                                    navController.navigate("calories")
                                },

                                onNavigateToCaloriesHistory = {
                                    navController.navigate("calories_history")
                                },

                                onNavigateToCaloriesStats = {
                                    navController.navigate("calories_stats")
                                }
                            )
                        }
                    }

                    /// MAP
                    composable("map") {
                        MapScreen()
                    }

                    /// EQUIPMENT
                    composable("equipment") {
                        EquipmentScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    /// CALCULATOR
                    composable("calories") {
                        userIdState?.let { uid ->
                            CaloriesScreen(
                                userId = uid,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }

                    /// HISTORY
                    composable("calories_history") {
                        userIdState?.let { uid ->
                            CaloriesHistoryScreen(
                                userId = uid,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }

                    /// STATS
                    composable("calories_stats") {
                        userIdState?.let { uid ->
                            CaloriesStatsScreen(
                                userId = uid,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
