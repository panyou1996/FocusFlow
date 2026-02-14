package com.panyou.focusflow.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var syncEnabled by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Account Section
        Text(
            text = "Account",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            ListItem(
                headlineContent = { Text("Sign in") },
                supportingContent = { Text("Sync your tasks across devices") },
                leadingContent = {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Sync Section
        Text(
            text = "Sync",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            ListItem(
                headlineContent = { Text("Cloud Sync") },
                supportingContent = { Text("Sync with Supabase (Coming Soon)") },
                leadingContent = {
                    Icon(Icons.Default.CloudSync, contentDescription = null)
                },
                trailingContent = {
                    Switch(
                        checked = syncEnabled,
                        onCheckedChange = { syncEnabled = it },
                        enabled = false // Not implemented yet
                    )
                }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // About Section
        Text(
            text = "About",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            onClick = { showAbout = true }
        ) {
            ListItem(
                headlineContent = { Text("About FocusFlow") },
                supportingContent = { Text("Version 1.0.0") },
                leadingContent = {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
            )
        }
        
        if (showAbout) {
            AlertDialog(
                onDismissRequest = { showAbout = false },
                title = { Text("FocusFlow") },
                text = {
                    Column {
                        Text("Version 1.0.0")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "A beautiful task app built with Kotlin, Jetpack Compose, and Material Design 3.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Features:",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text("• MD3 Expressive UI")
                        Text("• Offline-first with Room")
                        Text("• AI-powered suggestions")
                        Text("• Supabase sync ready")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAbout = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
