package com.trailmate.app.ui.calories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmate.app.models.CaloriesHistory
import com.trailmate.app.ui.components.AppTopBar

@Composable
fun CaloriesHistoryScreen(
    userId: Int,
    onBack: () -> Unit
) {

    val vm: CaloriesViewModel = viewModel()

    LaunchedEffect(userId) {
        vm.loadHistory(userId)
    }

    Scaffold(
        topBar = { AppTopBar("Calories History", onBack) }
    ) { pad ->

        if (vm.isLoading) {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .padding(pad)
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            items(vm.historyList) { item: CaloriesHistory ->

                val isCycling =
                    item.activity.equals("cycling", true)

                val isTrekking =
                    item.activity.equals("trekking", true)

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                )
                {


                Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // ICON BADGE
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(
                                    if (isCycling)
                                        Color(0xFFE3F2FD)
                                    else
                                        Color(0xFFE8F5E9),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector =
                                    when {
                                        isCycling -> Icons.Default.DirectionsBike
                                        isTrekking -> Icons.Default.Hiking
                                        else -> Icons.AutoMirrored.Filled.DirectionsWalk
                                    },
                                contentDescription = null,
                                tint =
                                    if (isCycling)
                                        Color(0xFF1976D2)
                                    else
                                        Color(0xFF2E7D32)
                            )
                        }

                        Spacer(Modifier.width(14.dp))

                        Column {

                            Text(
                                item.activity,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(6.dp))

                            Text("🔥 ${item.calories} kcal")
                            Text("📏 ${item.distance} km")
                            Text("⏱ ${item.duration} hr")
                        }
                    }
                }
            }
        }
    }
}
