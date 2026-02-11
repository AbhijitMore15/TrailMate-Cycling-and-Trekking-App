package com.trailmate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trailmate.app.ui.map.MapScreen
import com.trailmate.app.ui.home.HomeScreen
import com.trailmate.app.ui.login.LoginScreen
import com.trailmate.app.ui.login.RegisterScreen
import com.trailmate.app.ui.theme.TrailMateTheme
import com.trailmate.app.utils.DataStoreManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ DataStore Init
        dataStoreManager = DataStoreManager(this)

        setContent {
            TrailMateTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"   // 🔥 Stable base (No auto login)
                ) {

                    // ✅ LOGIN
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { token, userId ->

                                lifecycleScope.launch {
                                    dataStoreManager.saveAuthToken(token)
                                    dataStoreManager.saveUserId(userId)
                                }

                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                navController.navigate("register")
                            }
                        )
                    }

                    // ✅ REGISTER
                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = { token, userId ->

                                lifecycleScope.launch {
                                    dataStoreManager.saveAuthToken(token)
                                    dataStoreManager.saveUserId(userId)
                                }

                                navController.navigate("home") {
                                    popUpTo("register") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // ✅ HOME
                    composable("home") {
                        HomeScreen(
                            onNavigateToMap = {
                                navController.navigate("map")
                            },
                            onLogout = {
                                lifecycleScope.launch {
                                    dataStoreManager.clearAllData()

                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            },
                            token = "",
                            userId = 0
                        )
                    }

                    // 🗺️ MAP SCREEN
                    composable("map") {
                        MapScreen()
                    }

                    // Other screens
                    composable("tracking") { }

                    composable("activity_history") { }
                }
            }
        }
    }
}