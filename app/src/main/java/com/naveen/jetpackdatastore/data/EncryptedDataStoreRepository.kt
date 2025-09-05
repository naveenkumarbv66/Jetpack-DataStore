package com.naveen.jetpackdatastore.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import android.util.Base64
import com.naveen.jetpackdatastore.crypto.EncryptionProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.google.gson.JsonParser

private val Context.dataStore by preferencesDataStore(name = "NaveenPrivateDataBase")

class EncryptedDataStoreRepository(private val appContext: Context) {

    private val counterKey: Preferences.Key<Int> = intPreferencesKey("counter")
    private val nameKey = stringPreferencesKey("name")
    private val ageKey = stringPreferencesKey("age")
    private val genderKey = stringPreferencesKey("isGender")
    private val imageKey = stringPreferencesKey("userImage")
    private val jsonObjectKey = stringPreferencesKey("jsonObject")
    private val jsonArrayKey = stringPreferencesKey("jsonArray")
    
    private val gson = Gson()

    private fun aead(): Aead = EncryptionProvider.getAead(appContext)

    private fun encrypt(plain: String): String {
        val cipher = aead().encrypt(plain.toByteArray(), null)
        return Base64.encodeToString(cipher, Base64.NO_WRAP)
    }

    private fun decrypt(cipherB64: String): String {
        val cipher = Base64.decode(cipherB64, Base64.NO_WRAP)
        val plain = aead().decrypt(cipher, null)
        return String(plain)
    }

    private fun encrypt(bytes: ByteArray): String {
        val cipher = aead().encrypt(bytes, null)
        return Base64.encodeToString(cipher, Base64.NO_WRAP)
    }

    private fun decryptBytes(cipherB64: String): ByteArray {
        val cipher = Base64.decode(cipherB64, Base64.NO_WRAP)
        return aead().decrypt(cipher, null)
    }

    suspend fun save(text: String) {
        val encrypted = encrypt(text)
        appContext.dataStore.edit { prefs ->
            val index = (prefs[counterKey] ?: 0) + 1
            prefs[counterKey] = index
            val key = stringPreferencesKey("item_" + index)
            prefs[key] = encrypted
        }
    }

    fun readAll(): Flow<List<String>> {
        return appContext.dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs ->
                val max = prefs[counterKey] ?: 0
                if (max == 0) return@map emptyList()
                val result = mutableListOf<String>()
                for (i in 1..max) {
                    val key = stringPreferencesKey("item_" + i)
                    val enc = prefs[key] ?: continue
                    result.add(decrypt(enc))
                }
                result
            }
    }

    suspend fun deleteAll() {
        appContext.dataStore.edit { prefs ->
            val keysToRemove = prefs.asMap().keys.filter { key ->
                key.name.startsWith("item_") || key.name == counterKey.name
            }
            keysToRemove.forEach { prefs.remove(it) }
        }
    }

    // Specific getters/setters
    suspend fun setName(name: String) {
        val enc = encrypt(name)
        appContext.dataStore.edit { it[nameKey] = enc }
    }

    fun getName(): Flow<String?> = appContext.dataStore.data.map { prefs ->
        prefs[nameKey]?.let { decrypt(it) }
    }

    suspend fun setAge(age: Int) {
        val enc = encrypt(age.toString())
        appContext.dataStore.edit { it[ageKey] = enc }
    }

    fun getAge(): Flow<Int?> = appContext.dataStore.data.map { prefs ->
        prefs[ageKey]?.let { runCatching { decrypt(it).toInt() }.getOrNull() }
    }

    suspend fun setIsGender(isGender: Boolean) {
        val enc = encrypt(if (isGender) "true" else "false")
        appContext.dataStore.edit { it[genderKey] = enc }
    }

    fun getIsGender(): Flow<Boolean?> = appContext.dataStore.data.map { prefs ->
        prefs[genderKey]?.let { decrypt(it).toBooleanStrictOrNull() ?: decrypt(it).equals("true", ignoreCase = true) }
    }

    suspend fun setUserImage(imageBytes: ByteArray) {
        val enc = encrypt(imageBytes)
        appContext.dataStore.edit { it[imageKey] = enc }
    }

    fun getUserImage(): Flow<ByteArray?> = appContext.dataStore.data.map { prefs ->
        prefs[imageKey]?.let { decryptBytes(it) }
    }

    // JSON Object operations
    suspend fun saveJsonObject(jsonObject: JsonObject) {
        val jsonString = gson.toJson(jsonObject)
        val encrypted = encrypt(jsonString)
        appContext.dataStore.edit { it[jsonObjectKey] = encrypted }
    }

    suspend fun saveJsonObject(jsonString: String) {
        try {
            // Validate JSON format
            JsonParser.parseString(jsonString)
            val encrypted = encrypt(jsonString)
            appContext.dataStore.edit { it[jsonObjectKey] = encrypted }
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JSON format: ${e.message}")
        }
    }

    fun getJsonObject(): Flow<JsonObject?> = appContext.dataStore.data.map { prefs ->
        prefs[jsonObjectKey]?.let { encryptedJson ->
            try {
                val decryptedJson = decrypt(encryptedJson)
                JsonParser.parseString(decryptedJson).asJsonObject
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getJsonObjectAsString(): Flow<String?> = appContext.dataStore.data.map { prefs ->
        prefs[jsonObjectKey]?.let { decrypt(it) }
    }

    suspend fun clearJsonObject() {
        appContext.dataStore.edit { it.remove(jsonObjectKey) }
    }

    // JSON Array operations
    suspend fun saveJsonArray(jsonArray: JsonArray) {
        val jsonString = gson.toJson(jsonArray)
        val encrypted = encrypt(jsonString)
        appContext.dataStore.edit { it[jsonArrayKey] = encrypted }
    }

    suspend fun saveJsonArray(jsonString: String) {
        try {
            // Validate JSON array format
            val parsed = JsonParser.parseString(jsonString)
            if (!parsed.isJsonArray) {
                throw IllegalArgumentException("Input must be a valid JSON array")
            }
            val encrypted = encrypt(jsonString)
            appContext.dataStore.edit { it[jsonArrayKey] = encrypted }
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JSON array format: ${e.message}")
        }
    }

    suspend fun saveJsonArray(list: List<Any>) {
        val jsonString = gson.toJson(list)
        val encrypted = encrypt(jsonString)
        appContext.dataStore.edit { it[jsonArrayKey] = encrypted }
    }

    fun getJsonArray(): Flow<JsonArray?> = appContext.dataStore.data.map { prefs ->
        prefs[jsonArrayKey]?.let { encryptedJson ->
            try {
                val decryptedJson = decrypt(encryptedJson)
                JsonParser.parseString(decryptedJson).asJsonArray
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getJsonArrayAsString(): Flow<String?> = appContext.dataStore.data.map { prefs ->
        prefs[jsonArrayKey]?.let { decrypt(it) }
    }

    fun getJsonArrayAsList(): Flow<List<Any>?> = appContext.dataStore.data.map { prefs ->
        prefs[jsonArrayKey]?.let { encryptedJson ->
            try {
                val decryptedJson = decrypt(encryptedJson)
                val jsonArray = JsonParser.parseString(decryptedJson).asJsonArray
                val list = mutableListOf<Any>()
                jsonArray.forEach { element ->
                    when {
                        element.isJsonPrimitive -> {
                            val primitive = element.asJsonPrimitive
                            when {
                                primitive.isString -> list.add(primitive.asString)
                                primitive.isNumber -> list.add(primitive.asNumber)
                                primitive.isBoolean -> list.add(primitive.asBoolean)
                                else -> list.add(primitive.toString())
                            }
                        }
                        element.isJsonObject -> list.add(element.asJsonObject)
                        element.isJsonArray -> list.add(element.asJsonArray)
                        else -> list.add(element.toString())
                    }
                }
                list
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun clearJsonArray() {
        appContext.dataStore.edit { it.remove(jsonArrayKey) }
    }

    // Utility methods for JSON Array manipulation
    suspend fun addToJsonArray(item: Any) {
        val currentArray = getJsonArrayAsString()
        // Note: This is a simplified approach. In a real app, you might want to use a more sophisticated approach
        // that properly handles concurrent modifications
        appContext.dataStore.data.collect { prefs ->
            val existingJson = prefs[jsonArrayKey]
            val currentArray = if (existingJson != null) {
                try {
                    JsonParser.parseString(decrypt(existingJson)).asJsonArray
                } catch (e: Exception) {
                    JsonArray()
                }
            } else {
                JsonArray()
            }
            
            // Add new item to array
            val newItem = gson.toJsonTree(item)
            currentArray.add(newItem)
            
            // Save updated array
            val jsonString = gson.toJson(currentArray)
            val encrypted = encrypt(jsonString)
            appContext.dataStore.edit { it[jsonArrayKey] = encrypted }
        }
    }

    suspend fun removeFromJsonArray(index: Int) {
        appContext.dataStore.data.collect { prefs ->
            val existingJson = prefs[jsonArrayKey]
            if (existingJson != null) {
                try {
                    val currentArray = JsonParser.parseString(decrypt(existingJson)).asJsonArray
                    if (index >= 0 && index < currentArray.size()) {
                        currentArray.remove(index)
                        val jsonString = gson.toJson(currentArray)
                        val encrypted = encrypt(jsonString)
                        appContext.dataStore.edit { it[jsonArrayKey] = encrypted }
                    }
                } catch (e: Exception) {
                    // Handle error silently or log it
                }
            }
        }
    }
}


