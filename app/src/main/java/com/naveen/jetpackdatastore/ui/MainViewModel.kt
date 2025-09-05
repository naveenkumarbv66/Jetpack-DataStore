package com.naveen.jetpackdatastore.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.naveen.jetpackdatastore.data.EncryptedDataStoreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.google.gson.JsonObject
import com.google.gson.JsonArray

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EncryptedDataStoreRepository(application.applicationContext)

    val items: StateFlow<List<String>> = repository.readAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val name: StateFlow<String?> = repository.getName()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val age: StateFlow<Int?> = repository.getAge()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val isGender: StateFlow<Boolean?> = repository.getIsGender()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val userImage: StateFlow<ByteArray?> = repository.getUserImage()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val jsonObject: StateFlow<JsonObject?> = repository.getJsonObject()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val jsonObjectAsString: StateFlow<String?> = repository.getJsonObjectAsString()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val jsonArray: StateFlow<JsonArray?> = repository.getJsonArray()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val jsonArrayAsString: StateFlow<String?> = repository.getJsonArrayAsString()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val jsonArrayAsList: StateFlow<List<Any>?> = repository.getJsonArrayAsList()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun save(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch { repository.save(text.trim()) }
    }

    fun deleteAll() {
        viewModelScope.launch { repository.deleteAll() }
    }

    fun setName(value: String) {
        viewModelScope.launch { repository.setName(value) }
    }

    fun setAge(value: Int) {
        viewModelScope.launch { repository.setAge(value) }
    }

    fun setIsGender(value: Boolean) {
        viewModelScope.launch { repository.setIsGender(value) }
    }

    fun setUserImage(bytes: ByteArray) {
        viewModelScope.launch { repository.setUserImage(bytes) }
    }

    // JSON Object operations
    fun saveJsonObject(jsonObject: JsonObject) {
        viewModelScope.launch { repository.saveJsonObject(jsonObject) }
    }

    fun saveJsonObject(jsonString: String) {
        viewModelScope.launch { 
            try {
                repository.saveJsonObject(jsonString)
            } catch (e: Exception) {
                // Handle JSON validation error
                // You might want to expose this error to the UI
            }
        }
    }

    fun clearJsonObject() {
        viewModelScope.launch { repository.clearJsonObject() }
    }

    // JSON Array operations
    fun saveJsonArray(jsonArray: JsonArray) {
        viewModelScope.launch { repository.saveJsonArray(jsonArray) }
    }

    fun saveJsonArray(jsonString: String) {
        viewModelScope.launch { 
            try {
                repository.saveJsonArray(jsonString)
            } catch (e: Exception) {
                // Handle JSON validation error
                // You might want to expose this error to the UI
            }
        }
    }

    fun saveJsonArray(list: List<Any>) {
        viewModelScope.launch { repository.saveJsonArray(list) }
    }

    fun addToJsonArray(item: Any) {
        viewModelScope.launch { repository.addToJsonArray(item) }
    }

    fun removeFromJsonArray(index: Int) {
        viewModelScope.launch { repository.removeFromJsonArray(index) }
    }

    fun clearJsonArray() {
        viewModelScope.launch { repository.clearJsonArray() }
    }
}


