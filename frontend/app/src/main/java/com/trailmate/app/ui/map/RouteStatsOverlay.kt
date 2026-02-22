package com.trailmate.app.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trailmate.app.models.RouteStats

@Composable
fun RouteStatsOverlay(
    stats: RouteStats,
    modifier: Modifier = Modifier
) {

    if (stats.distanceMeters == 0f) return

    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier
    ) {

        Column(
            Modifier
                .background(Color.White)
                .padding(14.dp)
        ) {

            Text("Route Stats")

            Spacer(Modifier.height(8.dp))

            Text("Steps: ${stats.steps}")
            Text("Pedals: ${stats.pedals}")
            Text("ETA: ${stats.minutes} min")
        }
    }
}
