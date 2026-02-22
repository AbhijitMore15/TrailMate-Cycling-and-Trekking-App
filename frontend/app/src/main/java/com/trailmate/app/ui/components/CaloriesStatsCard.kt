package com.trailmate.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CaloriesStatsCard(
    total: Int,
    average: Int,
    onClick: () -> Unit
) {

    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(165.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFF4FFF7),
                            Color(0xFFE8F5EC)
                        )
                    )
                )
                .padding(20.dp)
        ) {

            Column {

                /// HEADER
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(8.dp, CircleShape)
                            .background(Color(0xFF34A853), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocalFireDepartment,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Text(
                        "Calories Summary",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(24.dp))

                /// STATS ROW
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    StatBlock("Total", "$total kcal")
                    StatBlock("Average", "$average kcal")
                }
            }
        }
    }
}

//////////////////////////////////////////////////////////////
// STAT BLOCK
//////////////////////////////////////////////////////////////

@Composable
private fun StatBlock(label: String, value: String) {

    Column {

        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )

        Spacer(Modifier.height(6.dp))

        Text(
            value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
