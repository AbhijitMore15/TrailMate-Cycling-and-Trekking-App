package com.trailmate.app.ui.calories

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmate.app.ui.components.AppTopBar
import com.trailmate.app.ui.components.Stepper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaloriesScreen(
    userId: Int,
    onBack: () -> Unit
) {

    val vm: CaloriesViewModel = viewModel()

    val activityOptions = listOf("Trekking", "Cycling")

    var expanded by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf(activityOptions[0]) }

    var duration by remember { mutableStateOf(2) }
    var distance by remember { mutableStateOf(5) }
    var weight by remember { mutableStateOf(70) }

    Scaffold(
        topBar = { AppTopBar("Calories Calculator", onBack) }
    ) { pad ->

        Column(
            Modifier
                .padding(pad)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // ---------- INPUT CARD ----------
            ElevatedCard {
                Column(Modifier.padding(16.dp)) {

                    Text(
                        "Enter Activity Details",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {

                        OutlinedTextField(
                            value = selectedActivity,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Activity") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            activityOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedActivity = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Stepper("Duration (hr)", duration) { duration = it }
                    Stepper("Distance (km)", distance) { distance = it }
                    Stepper("Weight (kg)", weight, 30) { weight = it }
                }
            }

            // ---------- BUTTON ----------
            Button(
                onClick = {
                    vm.calculateCalories(
                        userId = userId,
                        activity = selectedActivity.lowercase(),
                        duration = duration.toDouble(),
                        distance = distance.toDouble(),
                        weight = weight.toDouble()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate Calories")
            }

            // ---------- LOADING ----------
            if (vm.isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            // ---------- ERROR ----------
            vm.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // ---------- RESULT ----------
            vm.caloriesResult?.let { result ->
                ElevatedCard {
                    Column(
                        Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Calories Burned",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "${result.toInt()} kcal",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}
