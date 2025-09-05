package com.naveen.jetpackdatastore.proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object UserSettingsSerializer : Serializer<UserSettings> {
    override val defaultValue: UserSettings = UserSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSettings {
        try {
            return UserSettings.parseFrom(input)
        } catch (exception: IOException) {
            throw exception
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) = t.writeTo(output)
}

private val Context.userSettingsDataStore: DataStore<UserSettings> by dataStore(
    fileName = "user_settings.pb",
    serializer = UserSettingsSerializer
)

class ProtoDataStoreRepository(private val context: Context) {

    val userSettings: Flow<UserSettings> = context.userSettingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(UserSettings.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateUserName(name: String) {
        context.userSettingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setUserName(name)
                .build()
        }
    }

    suspend fun updateUserAge(age: Int) {
        context.userSettingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setUserAge(age)
                .build()
        }
    }

    suspend fun updatePremiumStatus(isPremium: Boolean) {
        context.userSettingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setIsPremiumUser(isPremium)
                .build()
        }
    }

    suspend fun updateEmail(email: String) {
        context.userSettingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setEmailAddress(email)
                .build()
        }
    }

    suspend fun addFavoriteTopic(topic: String) {
        context.userSettingsDataStore.updateData { currentSettings ->
            val currentTopics = currentSettings.favoriteTopicsList.toMutableList()
            if (!currentTopics.contains(topic)) {
                currentTopics.add(topic)
            }
            currentSettings.toBuilder()
                .clearFavoriteTopics()
                .addAllFavoriteTopics(currentTopics)
                .build()
        }
    }

    suspend fun removeFavoriteTopic(topic: String) {
        context.userSettingsDataStore.updateData { currentSettings ->
            val currentTopics = currentSettings.favoriteTopicsList.toMutableList()
            currentTopics.remove(topic)
            currentSettings.toBuilder()
                .clearFavoriteTopics()
                .addAllFavoriteTopics(currentTopics)
                .build()
        }
    }

    suspend fun updateTheme(primaryColor: String, isDarkMode: Boolean, fontSize: String) {
        context.userSettingsDataStore.updateData { currentSettings ->
            val theme = UserTheme.newBuilder()
                .setPrimaryColor(primaryColor)
                .setIsDarkMode(isDarkMode)
                .setFontSize(fontSize)
                .build()

            currentSettings.toBuilder()
                .setTheme(theme)
                .build()
        }
    }

    suspend fun updateNotificationSettings(
        email: Boolean,
        push: Boolean,
        sms: Boolean,
        frequency: Int
    ) {
        context.userSettingsDataStore.updateData { currentSettings ->
            val notifications = NotificationSettings.newBuilder()
                .setEmailNotifications(email)
                .setPushNotifications(push)
                .setSmsNotifications(sms)
                .setNotificationFrequency(frequency)
                .build()

            currentSettings.toBuilder()
                .setNotifications(notifications)
                .build()
        }
    }

    suspend fun clearAllData() {
        context.userSettingsDataStore.updateData { 
            UserSettings.getDefaultInstance()
        }
    }

    // Convenience getters
    fun getUserName(): Flow<String> = userSettings.map { it.userName }
    fun getUserAge(): Flow<Int> = userSettings.map { it.userAge }
    fun getIsPremium(): Flow<Boolean> = userSettings.map { it.isPremiumUser }
    fun getEmail(): Flow<String> = userSettings.map { it.emailAddress }
    fun getFavoriteTopics(): Flow<List<String>> = userSettings.map { it.favoriteTopicsList }
    fun getTheme(): Flow<UserTheme> = userSettings.map { it.theme }
    fun getNotifications(): Flow<NotificationSettings> = userSettings.map { it.notifications }
}
