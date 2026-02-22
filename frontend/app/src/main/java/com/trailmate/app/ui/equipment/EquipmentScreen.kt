package com.trailmate.app.ui.equipment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentScreen(
    onBack: () -> Unit = {}
) {

    val viewModel: EquipmentViewModel = viewModel()

    // =========================
    // STATE
    // =========================
    val activityOptions = listOf("Trekking", "Cycling")
    val difficultyOptions = listOf("Beginner", "Intermediate", "Advanced")

    var selectedActivity by remember { mutableStateOf(activityOptions[0]) }
    var selectedDifficulty by remember { mutableStateOf(difficultyOptions[0]) }

    var activityExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }

    var duration by remember { mutableStateOf(2) }

    // =========================
    // UI
    // =========================
    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Equipment Recommendation") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            /// INPUT CARD
            item {
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        /// ACTIVITY
                        ExposedDropdownMenuBox(
                            expanded = activityExpanded,
                            onExpandedChange = { activityExpanded = !activityExpanded }
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
                                expanded = activityExpanded,
                                onDismissRequest = { activityExpanded = false }
                            ) {
                                activityOptions.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            selectedActivity = it
                                            activityExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        /// DIFFICULTY
                        ExposedDropdownMenuBox(
                            expanded = difficultyExpanded,
                            onExpandedChange = { difficultyExpanded = !difficultyExpanded }
                        ) {

                            OutlinedTextField(
                                value = selectedDifficulty,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Difficulty") },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = difficultyExpanded,
                                onDismissRequest = { difficultyExpanded = false }
                            ) {
                                difficultyOptions.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            selectedDifficulty = it
                                            difficultyExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        /// DURATION STEPPER
                        Column {

                            Text(
                                "Duration (hours)",
                                style = MaterialTheme.typography.labelLarge
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                IconButton(
                                    onClick = { if (duration > 1) duration-- }
                                ) {
                                    Icon(Icons.Default.Remove, null)
                                }

                                Text(
                                    duration.toString(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                IconButton(
                                    onClick = { duration++ }
                                ) {
                                    Icon(Icons.Default.Add, null)
                                }
                            }
                        }

                        /// BUTTON
                        Button(
                            onClick = {
                                viewModel.getEquipment(
                                    selectedActivity.lowercase(),
                                    selectedDifficulty.lowercase(),
                                    duration
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Get Equipment")
                        }
                    }
                }
            }

            /// LOADING
            if (viewModel.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            /// ERROR
            viewModel.errorMessage?.let {
                item {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            /// RESULT HEADER
            if (viewModel.equipmentList.isNotEmpty()) {
                item {
                    Text(
                        "Recommended Gear",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            /// RESULTS
            items(viewModel.equipmentList) { item ->
                EquipmentCard(item)
            }
        }
    }
}
