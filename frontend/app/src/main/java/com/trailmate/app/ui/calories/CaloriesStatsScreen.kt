package com.trailmate.app.ui.calories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmate.app.ui.components.AppTopBar

@Composable
fun CaloriesStatsScreen(
    userId: Int,
    onBack: () -> Unit
) {

    val vm: CaloriesViewModel = viewModel()

    LaunchedEffect(userId) {
        vm.loadHistory(userId)
    }

    Scaffold(
        topBar = { AppTopBar("Statistics", onBack) }
    ) { pad ->

        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ---------- STATS ----------
            item {
                StatCard(
                    "Total Calories",
                    vm.totalCalories.toInt().toString()
                )
            }

            item {
                StatCard(
                    "Average Calories",
                    vm.avgCalories.toInt().toString()
                )
            }

            // ---------- TITLE ----------
            item {
                Text(
                    "Recent Activity",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // ---------- LIST ----------
            items(vm.historyList.takeLast(5)) { item ->

                val isCycling =
                    item.activity.equals("cycling", ignoreCase = true)

                ListItem(

                    leadingContent = {
                        Icon(
                            imageVector =
                                if (isCycling)
                                    Icons.Default.DirectionsBike
                                else
                                    Icons.AutoMirrored.Filled.DirectionsRun,

                            contentDescription = null,

                            tint =
                                if (isCycling)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondary
                        )
                    },

                    headlineContent = {
                        Text(item.activity)
                    },

                    supportingContent = {
                        Text("${item.calories} kcal")
                    }
                )

                Divider()
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {

    ElevatedCard {
        Column(Modifier.padding(18.dp)) {

            Text(
                title,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.height(4.dp))

            Text(
                value,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
