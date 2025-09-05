package com.naveen.jetpackdatastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naveen.jetpackdatastore.ui.MainViewModel
import com.naveen.jetpackdatastore.ui.theme.JetpackDataStoreTheme
import androidx.compose.ui.text.input.KeyboardType
import android.content.Intent
import androidx.compose.ui.Alignment
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.google.gson.JsonParser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackDataStoreTheme {
                val viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    val items by viewModel.items.collectAsStateWithLifecycle()
    var nameInput by remember { mutableStateOf("") }
    var ageInput by remember { mutableStateOf("") }
    var genderInput by remember { mutableStateOf(false) }
    var showName by remember { mutableStateOf(false) }
    var showAge by remember { mutableStateOf(false) }
    var showGender by remember { mutableStateOf(false) }
    var showImage by remember { mutableStateOf(false) }
    var jsonInput by remember { mutableStateOf("") }
    var showJson by remember { mutableStateOf(false) }
    var jsonArrayInput by remember { mutableStateOf("") }
    var showJsonArray by remember { mutableStateOf(false) }
    var arrayItemInput by remember { mutableStateOf("") }
    val name by viewModel.name.collectAsStateWithLifecycle()
    val age by viewModel.age.collectAsStateWithLifecycle()
    val isGender by viewModel.isGender.collectAsStateWithLifecycle()
    val userImage by viewModel.userImage.collectAsStateWithLifecycle()
    val jsonObject by viewModel.jsonObject.collectAsStateWithLifecycle()
    val jsonObjectAsString by viewModel.jsonObjectAsString.collectAsStateWithLifecycle()
    val jsonArray by viewModel.jsonArray.collectAsStateWithLifecycle()
    val jsonArrayAsString by viewModel.jsonArrayAsString.collectAsStateWithLifecycle()
    val jsonArrayAsList by viewModel.jsonArrayAsList.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter text") }
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.save(input); input = "" }, modifier = Modifier.fillMaxWidth()) {
                Text("Save to DataStore")
            }
            Button(onClick = { /* Just reads via state */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Show all saved data")
            }
            Button(onClick = { viewModel.deleteAll() }, modifier = Modifier.fillMaxWidth()) {
                Text("Delete all data")
            }
            Button(
                onClick = { 
                    context.startActivity(Intent(context, ProtoDataStoreDemo::class.java))
                }, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Proto DataStore Demo")
            }
        }

        Text("Saved items:")
        Column {
            items.forEach { item ->
                Text(text = item, modifier = Modifier.padding(vertical = 2.dp))
            }
        }

        Text("Typed fields (encrypted):")
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.setName(nameInput) }, modifier = Modifier.weight(1f)) {
                Text("Save Name")
            }
            Button(onClick = { showName = true }, modifier = Modifier.weight(1f)) {
                Text("Show Name")
            }
        }
        if (showName) {
            Text(text = "Name: ${name ?: "-"}")
        }

        OutlinedTextField(
            value = ageInput,
            onValueChange = { ageInput = it.filter { ch -> ch.isDigit() } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Age") }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { ageInput.toIntOrNull()?.let { viewModel.setAge(it) } }, modifier = Modifier.weight(1f)) {
                Text("Save Age")
            }
            Button(onClick = { showAge = true }, modifier = Modifier.weight(1f)) {
                Text("Show Age")
            }
        }
        if (showAge) {
            Text(text = "Age: ${age?.toString() ?: "-"}")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Switch(checked = genderInput, onCheckedChange = { genderInput = it })
            Button(onClick = { viewModel.setIsGender(genderInput) }, modifier = Modifier.weight(1f)) {
                Text("Save Gender")
            }
            Button(onClick = { showGender = true }, modifier = Modifier.weight(1f)) {
                Text("Show Gender")
            }
        }
        if (showGender) {
            Text(text = "Gender: ${isGender?.toString() ?: "-"}")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.setUserImage(byteArrayOf(1,2,3,4,5)) }, modifier = Modifier.weight(1f)) {
                Text("Set Sample Image")
            }
            Button(onClick = { showImage = true }, modifier = Modifier.weight(1f)) {
                Text("Show Image")
            }
        }
        if (showImage) {
            val size = userImage?.size ?: 0
            Text(text = "Image bytes: $size")
        }

        // JSON Object Section
        Text("JSON Object (encrypted):")
        OutlinedTextField(
            value = jsonInput,
            onValueChange = { jsonInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter JSON") },
            placeholder = { Text("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}") }
        )
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { 
                viewModel.saveJsonObject(jsonInput)
                jsonInput = ""
            }, modifier = Modifier.weight(1f)) {
                Text("Save JSON")
            }
            Button(onClick = { 
                // Create sample JSON object
                val sampleJson = JsonObject().apply {
                    addProperty("name", "Naveen")
                    addProperty("age", 25)
                    addProperty("city", "Bangalore")
                    addProperty("isDeveloper", true)
                    addProperty("skills", "Android, Kotlin, Compose")
                }
                viewModel.saveJsonObject(sampleJson)
            }, modifier = Modifier.weight(1f)) {
                Text("Save Sample")
            }
            Button(onClick = { showJson = true }, modifier = Modifier.weight(1f)) {
                Text("Show JSON")
            }
        }
        
        if (showJson) {
            Column {
                Text("JSON Object:")
                Text(
                    text = jsonObjectAsString ?: "No JSON data",
                    modifier = Modifier.padding(8.dp)
                )
                if (jsonObject != null) {
                    Text("Parsed JSON Properties:")
                    jsonObject?.let { json ->
                        json.entrySet().forEach { entry ->
                            Text("${entry.key}: ${entry.value}")
                        }
                    }
                }
            }
        }
        
        Button(onClick = { viewModel.clearJsonObject() }, modifier = Modifier.fillMaxWidth()) {
            Text("Clear JSON Object")
        }

        // JSON Array Section
        Text("JSON Array (encrypted):")
        OutlinedTextField(
            value = jsonArrayInput,
            onValueChange = { jsonArrayInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter JSON Array") },
            placeholder = { Text("[\"item1\", \"item2\", {\"name\": \"John\"}]") }
        )
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { 
                viewModel.saveJsonArray(jsonArrayInput)
                jsonArrayInput = ""
            }, modifier = Modifier.weight(1f)) {
                Text("Save Array")
            }
            Button(onClick = { 
                // Create sample JSON array
                val sampleArray = JsonArray().apply {
                    add("Android")
                    add("Kotlin")
                    add("Compose")
                    add(JsonObject().apply {
                        addProperty("name", "Naveen")
                        addProperty("age", 25)
                    })
                    add(true)
                    add(42)
                }
                viewModel.saveJsonArray(sampleArray)
            }, modifier = Modifier.weight(1f)) {
                Text("Save Sample")
            }
            Button(onClick = { showJsonArray = true }, modifier = Modifier.weight(1f)) {
                Text("Show Array")
            }
        }

        // Add item to array
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = arrayItemInput,
                onValueChange = { arrayItemInput = it },
                modifier = Modifier.weight(1f),
                label = { Text("Add Item") },
                placeholder = { Text("New item") }
            )
            Button(onClick = { 
                if (arrayItemInput.isNotBlank()) {
                    viewModel.addToJsonArray(arrayItemInput)
                    arrayItemInput = ""
                }
            }) {
                Text("Add")
            }
        }
        
        if (showJsonArray) {
            Column {
                Text("JSON Array:")
                Text(
                    text = jsonArrayAsString ?: "No JSON array data",
                    modifier = Modifier.padding(8.dp)
                )
                if (jsonArray != null) {
                    Text("Array Items:")
                    jsonArray?.let { array ->
                        array.forEachIndexed { index, element ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("[$index]: $element")
                                Button(onClick = { viewModel.removeFromJsonArray(index) }) {
                                    Text("Remove")
                                }
                            }
                        }
                    }
                }
                if (jsonArrayAsList != null) {
                    Text("As List:")
                    jsonArrayAsList?.forEachIndexed { index, item ->
                        Text("[$index]: $item")
                    }
                }
            }
        }
        
        Button(onClick = { viewModel.clearJsonArray() }, modifier = Modifier.fillMaxWidth()) {
            Text("Clear JSON Array")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackDataStoreTheme {
        // Preview placeholder
        Text("Preview")
    }
}