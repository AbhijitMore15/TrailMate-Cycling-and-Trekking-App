package com.trailmate.app.ui.equipment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trailmate.app.models.EquipmentResponse

@Composable
fun EquipmentCard(item: EquipmentResponse) {

    val icon = when (item.category.lowercase()) {
        "hydration" -> Icons.Default.WaterDrop
        "food" -> Icons.Default.Fastfood
        "safety" -> Icons.Default.Shield
        "repair" -> Icons.Default.Build
        "clothing" -> Icons.Default.Checkroom
        "navigation" -> Icons.Default.Explore
        "electronics" -> Icons.Default.BatteryFull
        else -> Icons.Default.Inventory
    }

    val badgeColor = when (item.priority.lowercase()) {
        "essential" -> Color(0xFF2E7D32)
        "recommended" -> Color(0xFFEF6C00)
        else -> Color.Gray
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp)
        ) {

            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    item.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    item.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        badgeColor,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {

                Text(
                    item.priority.uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
