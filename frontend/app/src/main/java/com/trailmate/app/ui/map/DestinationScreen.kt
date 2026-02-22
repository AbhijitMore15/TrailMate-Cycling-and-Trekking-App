package com.trailmate.app.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DestinationScreen(navController: NavController) {

    var lat by remember { mutableStateOf("") }
    var lng by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Enter Destination", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = lat,
            onValueChange = { lat = it },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = lng,
            onValueChange = { lng = it },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (lat.isNotBlank() && lng.isNotBlank()) {
                    navController.navigate("map/$lat/$lng")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Activity")
        }
    }
}
