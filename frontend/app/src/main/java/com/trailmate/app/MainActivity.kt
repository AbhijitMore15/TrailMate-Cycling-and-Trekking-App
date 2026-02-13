package com.trailmate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// import com.mappls.sdk.maps.Mappls   // Comment out for now
// import com.trailmate.app.ui.map.MapScreen  // Comment out for now
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

        // Comment out Mappls init for now
        // Mappls.getInstance(applicationContext)

        dataStoreManager = DataStoreManager(this)

        setContent {
            TrailMateTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {

                    // LOGIN
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

                    // REGISTER
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

                    // HOME
                    composable("home") {
                        HomeScreen(
                            // Map navigation removed for now
                            // onNavigateToMap = {
                            //     navController.navigate("map")
                            // },
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

                    // MAP SCREEN - REMOVED FOR NOW
                    // Will add back later after other features
                    // composable("map") {
                    //     MapScreen(
                    //         latitude = 28.6139,
                    //         longitude = 77.2090,
                    //         zoomLevel = 12.0
                    //     )
                    // }

                    composable("tracking") { }

                    composable("activity_history") { }
                }
            }
        }
    }
}