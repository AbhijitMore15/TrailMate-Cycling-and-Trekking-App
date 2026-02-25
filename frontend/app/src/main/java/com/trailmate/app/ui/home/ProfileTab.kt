package com.trailmate.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
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

@OptIn(ExperimentalMaterial3Api::class)
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
    var heightUnit by remember { mutableStateOf("cm") }
    var weight by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("Gain Strength") }

    var isEditing by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    // 🔥 New Calendar Note States
    var selectedDate by remember { mutableStateOf<Int?>(null) }
    var noteText by remember { mutableStateOf("") }
    var showSheet by remember { mutableStateOf(false) }

    val notes by dataStore.notesFlow.collectAsState(initial = emptyMap())
    val today = LocalDate.now().dayOfMonth

    LaunchedEffect(Unit) {
        dataStore.profileFlow.collect {
            height = it.height ?: ""
            weight = it.weight ?: ""
            goal = it.goal ?: "Gain Strength"

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
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                    )
                )
        ) {

            if (isSaved && !isEditing) {
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
                    modifier = Modifier.size(100.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        null,
                        modifier = Modifier.padding(25.dp),
                        tint = Color.DarkGray
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    name.ifBlank { "Your Name" },
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Column(
            Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ================= DISPLAY MODE =================
            if (isSaved && !isEditing) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        ProfileItem("Height", "$height $heightUnit")
                        ProfileItem("Weight", "$weight kg")
                        ProfileItem("Goal", goal)
                    }
                }
            }

            // ================= EDIT MODE =================
            else {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {

                        // HEIGHT
                        Text("Height", style = MaterialTheme.typography.labelMedium)

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            OutlinedTextField(
                                value = height,
                                onValueChange = { height = it.filter { ch -> ch.isDigit() } },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )

                            Spacer(Modifier.width(8.dp))

                            var expanded by remember { mutableStateOf(false) }

                            Box {
                                OutlinedButton(onClick = { expanded = true }) {
                                    Text(heightUnit)
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    listOf("cm", "ft").forEach {
                                        DropdownMenuItem(
                                            text = { Text(it) },
                                            onClick = {
                                                heightUnit = it
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // WEIGHT
                        Text("Weight (kg)", style = MaterialTheme.typography.labelMedium)

                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it.filter { ch -> ch.isDigit() } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(Modifier.height(16.dp))

                        // GOAL
                        Text("Goal", style = MaterialTheme.typography.labelMedium)

                        var goalExpanded by remember { mutableStateOf(false) }

                        Box {
                            OutlinedButton(
                                onClick = { goalExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(goal)
                            }

                            DropdownMenu(
                                expanded = goalExpanded,
                                onDismissRequest = { goalExpanded = false }
                            ) {
                                listOf(
                                    "Gain Strength",
                                    "Lose Weight",
                                    "Build Endurance",
                                    "Stay Fit"
                                ).forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            goal = it
                                            goalExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    dataStore.saveProfile(
                                        name,
                                        height,
                                        weight,
                                        goal
                                    )
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
                items(days) { day ->

                    val isToday = day == today
                    val hasNote = notes.containsKey(day.toString())

                    Surface(
                        shape = CircleShape,
                        shadowElevation = if (isToday) 6.dp else 2.dp,
                        color = if (isToday) Color(0xFF66BB6A) else Color.White,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(42.dp),
                        onClick = {
                            selectedDate = day
                            noteText = notes[day.toString()] ?: ""
                            showSheet = true
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "$day",
                                color = if (isToday) Color.White else Color.Black
                            )

                            if (hasNote) {
                                Spacer(Modifier.height(2.dp))
                                Box(
                                    Modifier
                                        .size(5.dp)
                                        .background(Color.Red, CircleShape)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // LOGOUT
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE),
                    contentColor = Color.Red
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("Logout")
            }

            Spacer(Modifier.height(40.dp))
        }
    }

    // ================= NOTE BOTTOM SHEET =================
    if (showSheet && selectedDate != null) {

        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    "Notes for Day $selectedDate",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Write your activity note...") }
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        scope.launch {
                            dataStore.saveNote(
                                selectedDate.toString(),
                                noteText
                            )
                        }
                        showSheet = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Note")
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(label, color = Color.Gray)
        Text(value.ifBlank { "-" }, style = MaterialTheme.typography.bodyLarge)
    }
}