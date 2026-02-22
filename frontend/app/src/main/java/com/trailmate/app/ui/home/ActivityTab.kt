package com.trailmate.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ActivityTab(navController: NavController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(top = 90.dp, bottom = 110.dp)
    ) {

        /// HEADER
        item {
            Text(
                "Activity Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        /// RECENT
        item {
            SectionHeader("Recent Activities")
            ActivityHistoryList()
        }

        /// TOOLS
        item {
            SectionHeader("Tools")

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {

                ToolCard("Equipment Recommendation", Icons.Default.DirectionsBike) {
                    navController.navigate("equipment?from=activity")
                }

                ToolCard("Calories Calculator", Icons.Default.LocalFireDepartment) {
                    navController.navigate("calories?from=activity")
                }

                ToolCard("Calories History", Icons.Default.History) {
                    navController.navigate("calories_history?from=activity")
                }
            }
        }
    }
}

//////////////////////////////////////////////////////////////
// HEADER
//////////////////////////////////////////////////////////////

@Composable
fun SectionHeader(title: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

//////////////////////////////////////////////////////////////
// TOOL CARD
//////////////////////////////////////////////////////////////

@Composable
fun ToolCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            /// ICON CONTAINER
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    "Open tool",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.weight(1f))

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

//////////////////////////////////////////////////////////////
// ACTIVITY HISTORY
//////////////////////////////////////////////////////////////

@Composable
fun ActivityHistoryList() {

    val fakeData = listOf(
        "Cycling — 2.3 km — 12 min",
        "Trekking — 1.1 km — 20 min"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        fakeData.forEach { activity ->

            val icon =
                when {
                    activity.contains("Cycling", true) ->
                        Icons.Default.DirectionsBike

                    activity.contains("Trekking", true) ->
                        Icons.Default.Terrain

                    else ->
                        Icons.Default.DirectionsRun
                }

            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(2.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Text(
                        text = activity,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
