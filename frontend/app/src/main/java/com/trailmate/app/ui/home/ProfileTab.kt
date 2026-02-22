package com.trailmate.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.trailmate.app.utils.DataStoreManager


@Composable
fun ProfileTab(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {

    val vm: ProfileViewModel = viewModel()
    val name by vm.name.collectAsState()

    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    val scope = rememberCoroutineScope()

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    val today = LocalDate.now().dayOfMonth

    // LOAD LOCAL PROFILE DATA
    LaunchedEffect(Unit) {
        dataStore.profileFlow.collect {
            height = it.height ?: ""
            weight = it.weight ?: ""
            goal = it.goal ?: ""

            isSaved = height.isNotBlank()
            isEditing = height.isBlank()
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
    ) {

        // ================= HEADER =================
        Box(
            Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                    )
                )
        ) {

            if (isSaved) {
                IconButton(
                    onClick = {
                        isEditing = true
                        isSaved = false
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Edit, null, tint = Color.White)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(90.dp)
                ) {
                    Icon(Icons.Default.Person, null, Modifier.padding(20.dp))
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    name.ifBlank { "Your Name" },
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }


        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ================= PROFILE CARD =================
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(Modifier.padding(18.dp)) {

                    // ---------- DISPLAY MODE ----------
                    if (isSaved && !isEditing) {

                        ProfileItem("Name", name)
                        ProfileItem("Height", height)
                        ProfileItem("Weight", weight)
                        ProfileItem("Goal", goal)
                    }

                    // ---------- EDIT MODE ----------
                    else {

                        OutlinedTextField(
                            value = name,
                            onValueChange = {},
                            enabled = false,
                            label = { Text("Name (from account)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(10.dp))

                        OutlinedTextField(
                            value = height,
                            onValueChange = { height = it },
                            label = { Text("Height") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(10.dp))

                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Weight") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(10.dp))

                        OutlinedTextField(
                            value = goal,
                            onValueChange = { goal = it },
                            label = { Text("Goal") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(14.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    dataStore.saveProfile(name, height, weight, goal)
                                }
                                isEditing = false
                                isSaved = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Check, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Save Profile")
                        }
                    }
                }
            }


            // ================= CALENDAR =================
            Text("Activity Calendar", style = MaterialTheme.typography.titleMedium)

            val days = (1..31).toList()

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(250.dp)
            ) {
                items(days.size) { i ->

                    val day = days[i]
                    val isToday = day == today

                    Box(
                        Modifier.padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Surface(
                            shape = CircleShape,
                            shadowElevation = if (isToday) 6.dp else 2.dp,
                            color = if (isToday)
                                Color(0xFF66BB6A)
                            else
                                Color.White
                        ) {

                            Box(
                                Modifier.size(42.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$day",
                                    color = if (isToday) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }


            Spacer(Modifier.height(20.dp))


            // ================= LOGOUT =================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .width(180.dp)
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Logout, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Logout")
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}


// ================= PROFILE ITEM COMPONENT =================
@Composable
fun ProfileItem(label: String, value: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF1F5F9)
        ) {
            Text(
                text = value.ifBlank { "-" },
                modifier = Modifier.padding(14.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
