package com.trailmate.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Stepper(
    label: String,
    value: Int,
    min: Int = 1,
    onChange: (Int) -> Unit
) {

    Column {

        Text(label, style = MaterialTheme.typography.labelLarge)

        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { if (value > min) onChange(value - 1) }) {
                Icon(Icons.Default.Remove, null)
            }

            Text(
                value.toString(),
                Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = { onChange(value + 1) }) {
                Icon(Icons.Default.Add, null)
            }
        }
    }
}
