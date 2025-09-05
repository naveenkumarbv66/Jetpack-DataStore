package com.naveen.jetpackdatastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.naveen.jetpackdatastore.proto.ProtoDemoViewModel
import com.naveen.jetpackdatastore.ui.theme.JetpackDataStoreTheme

class ProtoDataStoreDemo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackDataStoreTheme {
                val viewModel: ProtoDemoViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProtoDemoScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtoDemoScreen(viewModel: ProtoDemoViewModel, modifier: Modifier = Modifier) {
    val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val userAge by viewModel.userAge.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val favoriteTopics by viewModel.favoriteTopics.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    var nameInput by remember { mutableStateOf("") }
    var ageInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var topicInput by remember { mutableStateOf("") }
    var primaryColorInput by remember { mutableStateOf("") }
    var fontSizeInput by remember { mutableStateOf("") }
    var darkModeInput by remember { mutableStateOf(false) }
    var emailNotifInput by remember { mutableStateOf(false) }
    var pushNotifInput by remember { mutableStateOf(false) }
    var smsNotifInput by remember { mutableStateOf(false) }
    var frequencyInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Proto DataStore Demo",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // User Basic Info Section
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "User Information",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.updateUserName(nameInput) }) {
                            Text("Save Name")
                        }
                        Button(onClick = { nameInput = userName }) {
                            Text("Load Current")
                        }
                    }
                    
                    OutlinedTextField(
                        value = ageInput,
                        onValueChange = { ageInput = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { ageInput.toIntOrNull()?.let { viewModel.updateUserAge(it) } }) {
                            Text("Save Age")
                        }
                        Button(onClick = { ageInput = userAge.toString() }) {
                            Text("Load Current")
                        }
                    }
                    
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.updateEmail(emailInput) }) {
                            Text("Save Email")
                        }
                        Button(onClick = { emailInput = email }) {
                            Text("Load Current")
                        }
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = isPremium,
                            onCheckedChange = { viewModel.updatePremiumStatus(it) }
                        )
                        Text("Premium User")
                    }
                }
            }
        }

        // Favorite Topics Section
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Favorite Topics",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        label = { Text("Add Topic") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Button(onClick = { 
                        viewModel.addFavoriteTopic(topicInput)
                        topicInput = ""
                    }) {
                        Text("Add Topic")
                    }
                    
                    if (favoriteTopics.isNotEmpty()) {
                        Text("Current Topics:")
                        favoriteTopics.forEach { topic ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(topic)
                                Button(onClick = { viewModel.removeFavoriteTopic(topic) }) {
                                    Text("Remove")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Theme Settings Section
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Theme Settings",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = primaryColorInput,
                        onValueChange = { primaryColorInput = it },
                        label = { Text("Primary Color") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = fontSizeInput,
                        onValueChange = { fontSizeInput = it },
                        label = { Text("Font Size") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = darkModeInput,
                            onCheckedChange = { darkModeInput = it }
                        )
                        Text("Dark Mode")
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { 
                            viewModel.updateTheme(primaryColorInput, darkModeInput, fontSizeInput)
                        }) {
                            Text("Save Theme")
                        }
                        Button(onClick = { 
                            primaryColorInput = theme.primaryColor
                            fontSizeInput = theme.fontSize
                            darkModeInput = theme.isDarkMode
                        }) {
                            Text("Load Current")
                        }
                    }
                }
            }
        }

        // Notification Settings Section
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Notification Settings",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = emailNotifInput,
                            onCheckedChange = { emailNotifInput = it }
                        )
                        Text("Email Notifications")
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = pushNotifInput,
                            onCheckedChange = { pushNotifInput = it }
                        )
                        Text("Push Notifications")
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = smsNotifInput,
                            onCheckedChange = { smsNotifInput = it }
                        )
                        Text("SMS Notifications")
                    }
                    
                    OutlinedTextField(
                        value = frequencyInput,
                        onValueChange = { frequencyInput = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Frequency (minutes)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { 
                            frequencyInput.toIntOrNull()?.let { freq ->
                                viewModel.updateNotificationSettings(
                                    emailNotifInput, pushNotifInput, smsNotifInput, freq
                                )
                            }
                        }) {
                            Text("Save Notifications")
                        }
                        Button(onClick = { 
                            emailNotifInput = notifications.emailNotifications
                            pushNotifInput = notifications.pushNotifications
                            smsNotifInput = notifications.smsNotifications
                            frequencyInput = notifications.notificationFrequency.toString()
                        }) {
                            Text("Load Current")
                        }
                    }
                }
            }
        }

        // Current Data Display Section
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Current Data",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text("Name: $userName")
                    Text("Age: $userAge")
                    Text("Email: $email")
                    Text("Premium: $isPremium")
                    Text("Topics: ${favoriteTopics.joinToString(", ")}")
                    Text("Theme: ${theme.primaryColor}, Dark: ${theme.isDarkMode}, Font: ${theme.fontSize}")
                    Text("Notifications: Email=${notifications.emailNotifications}, Push=${notifications.pushNotifications}, SMS=${notifications.smsNotifications}, Freq=${notifications.notificationFrequency}")
                }
            }
        }

        // Clear Data Section
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Data Management",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Button(
                        onClick = { viewModel.clearAllData() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Clear All Data")
                    }
                }
            }
        }
    }
}
