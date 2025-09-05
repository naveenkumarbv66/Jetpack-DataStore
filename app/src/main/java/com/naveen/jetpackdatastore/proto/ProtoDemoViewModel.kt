package com.naveen.jetpackdatastore.proto

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProtoDemoViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = ProtoDataStoreRepository(application.applicationContext)

    // StateFlows for UI
    val userSettings: StateFlow<UserSettings> = repository.userSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings.getDefaultInstance())

    val userName: StateFlow<String> = repository.getUserName()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val userAge: StateFlow<Int> = repository.getUserAge()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val isPremium: StateFlow<Boolean> = repository.getIsPremium()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val email: StateFlow<String> = repository.getEmail()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val favoriteTopics: StateFlow<List<String>> = repository.getFavoriteTopics()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val theme: StateFlow<UserTheme> = repository.getTheme()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserTheme.getDefaultInstance())

    val notifications: StateFlow<NotificationSettings> = repository.getNotifications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NotificationSettings.getDefaultInstance())

    // Actions
    fun updateUserName(name: String) {
        viewModelScope.launch {
            repository.updateUserName(name)
        }
    }

    fun updateUserAge(age: Int) {
        viewModelScope.launch {
            repository.updateUserAge(age)
        }
    }

    fun updatePremiumStatus(isPremium: Boolean) {
        viewModelScope.launch {
            repository.updatePremiumStatus(isPremium)
        }
    }

    fun updateEmail(email: String) {
        viewModelScope.launch {
            repository.updateEmail(email)
        }
    }

    fun addFavoriteTopic(topic: String) {
        if (topic.isNotBlank()) {
            viewModelScope.launch {
                repository.addFavoriteTopic(topic.trim())
            }
        }
    }

    fun removeFavoriteTopic(topic: String) {
        viewModelScope.launch {
            repository.removeFavoriteTopic(topic)
        }
    }

    fun updateTheme(primaryColor: String, isDarkMode: Boolean, fontSize: String) {
        viewModelScope.launch {
            repository.updateTheme(primaryColor, isDarkMode, fontSize)
        }
    }

    fun updateNotificationSettings(
        email: Boolean,
        push: Boolean,
        sms: Boolean,
        frequency: Int
    ) {
        viewModelScope.launch {
            repository.updateNotificationSettings(email, push, sms, frequency)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
}
