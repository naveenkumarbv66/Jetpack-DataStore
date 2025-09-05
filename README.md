# Jetpack DataStore Features App

A comprehensive Android application demonstrating both **Preferences DataStore** with encryption and **Proto DataStore** with type-safe schemas, featuring MVVM architecture and Jetpack Compose UI.

## 🎯 Features Overview

### 🔐 **Encrypted Preferences DataStore** (MainActivity)
- **Tink Encryption**: Uses Google Tink for AES256-GCM encryption
- **Android Keystore**: Master keys protected by Android Keystore
- **Private Database**: All data stored in `NaveenPrivateDataBase` (app-internal storage)
- **Text Storage**: Save and retrieve multiple text entries
- **Typed Fields**: Store specific data types with encryption:
  - `name` (String)
  - `age` (Int) 
  - `isGender` (Boolean)
  - `userImage` (ByteArray)
- **JSON Objects**: Store and retrieve complex JSON data structures with encryption
- **JSON Arrays**: Store and manipulate JSON arrays with encryption and dynamic item management

### 📊 **Proto DataStore Demo** (ProtoDataStoreDemo)
- **Type-Safe Schema**: Protobuf messages with compile-time type checking
- **Complex Data Structures**: Nested objects and repeated fields
- **Schema Evolution**: Easy to add/modify fields without breaking existing data
- **Better Performance**: Binary serialization for faster I/O
- **User Settings**: Complete user profile with themes and notifications

### 🏗️ Architecture
- **MVVM Pattern**: Clean separation of concerns
- **Repository Pattern**: Centralized data access layer
- **Jetpack Compose**: Modern declarative UI
- **StateFlow**: Reactive data streams

## 📁 Project Structure

```
app/src/main/java/com/naveen/jetpackdatastore/
├── MainActivity.kt                    # Encrypted Preferences DataStore UI
├── ProtoDataStoreDemo.kt             # Proto DataStore Demo UI
├── crypto/
│   └── EncryptionProvider.kt         # Tink encryption setup
├── data/
│   └── EncryptedDataStoreRepository.kt # Preferences DataStore operations
├── proto/
│   ├── ProtoDataStoreRepository.kt   # Proto DataStore operations
│   └── ProtoDemoViewModel.kt         # Proto DataStore ViewModel
├── ui/
│   └── MainViewModel.kt              # Preferences DataStore ViewModel
└── proto/ (generated)
    └── UserSettings.java             # Generated protobuf classes

app/src/main/proto/
└── user_settings.proto               # Protobuf schema definition
```

## 📦 Dependencies

### Core DataStore
- **Preferences DataStore**: `androidx.datastore:datastore-preferences:1.1.1`
- **Proto DataStore**: `androidx.datastore:datastore-core:1.1.1`
- **Protobuf**: `com.google.protobuf:protobuf-javalite:3.25.1`

### Security & Encryption
- **Tink**: `com.google.crypto.tink:tink-android:1.13.0`

### JSON Processing
- **Gson**: `com.google.code.gson:gson:2.10.1`

### UI & Architecture
- **ViewModel Compose**: `androidx.lifecycle:lifecycle-viewmodel-compose:2.9.3`
- **Runtime Compose**: `androidx.lifecycle:lifecycle-runtime-compose:2.9.3`
- **Compose BOM**: `androidx.compose:compose-bom:2024.09.00`

## 🚀 Usage Guide

### 🔐 **Encrypted Preferences DataStore** (MainActivity)

#### Basic Text Operations
1. **Save Text**: Enter text in the field and tap "Save to DataStore"
2. **View All**: Tap "Show all saved data" to see the list
3. **Delete All**: Tap "Delete all data" to clear everything

#### Typed Field Operations
1. **Name**: Enter name → "Save Name" → "Show Name"
2. **Age**: Enter age (numbers only) → "Save Age" → "Show Age"  
3. **Gender**: Toggle switch → "Save Gender" → "Show Gender"
4. **Image**: "Set Sample Image" → "Show Image" (displays byte count)

#### JSON Object Operations
1. **Save Custom JSON**: Enter JSON string → "Save JSON"
2. **Save Sample JSON**: Tap "Save Sample" (creates sample JSON object)
3. **View JSON**: Tap "Show JSON" to display saved JSON data
4. **Clear JSON**: Tap "Clear JSON Object" to remove JSON data

#### JSON Array Operations
1. **Save Custom Array**: Enter JSON array string → "Save Array"
2. **Save Sample Array**: Tap "Save Sample" (creates sample JSON array with mixed types)
3. **Add Items**: Enter item in "Add Item" field → "Add" button
4. **Remove Items**: Tap "Remove" button next to any array item
5. **View Array**: Tap "Show Array" to display saved JSON array data
6. **Clear Array**: Tap "Clear JSON Array" to remove array data

### 📊 **Proto DataStore Demo** (ProtoDataStoreDemo)

#### Accessing the Demo
1. From MainActivity, tap "Open Proto DataStore Demo"
2. Explore the comprehensive user settings interface

#### User Information Section
- **Name**: Enter and save user name
- **Age**: Enter numeric age value
- **Email**: Set email address
- **Premium Status**: Toggle premium user status

#### Favorite Topics Management
- **Add Topics**: Enter topic name and tap "Add Topic"
- **Remove Topics**: Tap "Remove" next to any topic
- **View All**: See all saved topics in real-time

#### Theme Customization
- **Primary Color**: Set custom color value
- **Font Size**: Configure text size
- **Dark Mode**: Toggle dark/light theme
- **Save/Load**: Persist and restore theme settings

#### Notification Preferences
- **Email Notifications**: Enable/disable email alerts
- **Push Notifications**: Control push notification settings
- **SMS Notifications**: Manage SMS notification preferences
- **Frequency**: Set notification frequency in minutes

#### Data Management
- **Current Data Display**: View all saved data in real-time
- **Clear All Data**: Reset all user settings to defaults

## 📄 **JSON Object Examples**

### **Sample JSON Structure**
```json
{
  "name": "Naveen",
  "age": 25,
  "city": "Bangalore",
  "isDeveloper": true,
  "skills": "Android, Kotlin, Compose",
  "preferences": {
    "theme": "dark",
    "notifications": true
  },
  "projects": [
    "Jetpack DataStore App",
    "Compose UI Demo"
  ]
}
```

### **JSON Operations**
- **Save Custom**: Enter any valid JSON string
- **Save Sample**: Pre-defined JSON object with common fields
- **Display**: Shows both raw JSON and parsed properties
- **Encryption**: All JSON data encrypted before storage
- **Validation**: Invalid JSON format is rejected with error handling

## 📋 **JSON Array Examples**

### **Sample JSON Array Structure**
```json
[
  "Android",
  "Kotlin", 
  "Compose",
  {
    "name": "Naveen",
    "age": 25
  },
  true,
  42
]
```

### **JSON Array Operations**
- **Save Custom**: Enter any valid JSON array string
- **Save Sample**: Pre-defined JSON array with mixed data types
- **Add Items**: Dynamically add new items to existing array
- **Remove Items**: Remove specific items by index
- **Display**: Shows raw JSON, parsed array, and list formats
- **Encryption**: All JSON array data encrypted before storage
- **Validation**: Invalid JSON array format is rejected with error handling

## 🎨 **UI Improvements**

### **Smooth Scrolling**
- **Fixed Layout**: Replaced conflicting LazyColumn with scrollable Column
- **Single Scroll Container**: Entire MainActivity content scrolls smoothly
- **Responsive Design**: Works well on different screen sizes
- **No Layout Conflicts**: Removed scrolling issues between nested containers

### **Enhanced User Experience**
- **Intuitive Navigation**: Clear section separation and organized layout
- **Real-time Updates**: StateFlow integration for live data updates
- **Error Handling**: Graceful handling of invalid JSON inputs
- **Multiple Display Formats**: Raw JSON, parsed objects, and list views

## 🔒 Security Features

### Encrypted Preferences DataStore
- **Encryption at Rest**: All data encrypted before storage
- **Key Management**: Master keys stored in Android Keystore
- **Private Storage**: Database files in app-internal directory
- **API 24+ Compatible**: Uses `android.util.Base64` for compatibility

### Proto DataStore
- **Type Safety**: Compile-time type checking prevents runtime errors
- **Schema Validation**: Protobuf ensures data integrity
- **Private Storage**: Files stored in app-internal directory
- **No Encryption**: Proto DataStore focuses on type safety (encryption can be added separately)

## 🔧 Technical Details

### 🔐 **Encrypted Preferences DataStore Flow**
1. **Simple Data**: User input → Tink AEAD encryption → Base64 encoding → DataStore
2. **JSON Objects**: JSON → Gson serialization → Tink AEAD encryption → Base64 encoding → DataStore
3. **JSON Arrays**: JSON Array → Gson serialization → Tink AEAD encryption → Base64 encoding → DataStore
4. **Retrieval**: DataStore → Base64 decoding → Tink AEAD decryption → Gson deserialization → User output

### 📊 **Proto DataStore Schema**
```protobuf
message UserSettings {
  string user_name = 1;
  int32 user_age = 2;
  bool is_premium_user = 3;
  string email_address = 4;
  repeated string favorite_topics = 5;
  UserTheme theme = 6;
  NotificationSettings notifications = 7;
}

message UserTheme {
  string primary_color = 1;
  bool is_dark_mode = 2;
  string font_size = 3;
}

message NotificationSettings {
  bool email_notifications = 1;
  bool push_notifications = 2;
  bool sms_notifications = 3;
  int32 notification_frequency = 4;
}
```

### 🗂️ **DataStore Keys & Files**
#### Preferences DataStore
- **File**: `NaveenPrivateDataBase`
- **Keys**: `counter`, `item_N`, `name`, `age`, `isGender`, `userImage`, `jsonObject`, `jsonArray`

#### Proto DataStore
- **File**: `user_settings.pb`
- **Schema**: `UserSettings` protobuf message

### 📚 **Repository Methods**

#### Encrypted Preferences Repository
```kotlin
// Text operations
suspend fun save(text: String)
fun readAll(): Flow<List<String>>
suspend fun deleteAll()

// Typed operations
suspend fun setName(name: String)
fun getName(): Flow<String?>
suspend fun setAge(age: Int)
fun getAge(): Flow<Int?>
suspend fun setIsGender(isGender: Boolean)
fun getIsGender(): Flow<Boolean?>
suspend fun setUserImage(imageBytes: ByteArray)
fun getUserImage(): Flow<ByteArray?>

// JSON Object operations
suspend fun saveJsonObject(jsonObject: JsonObject)
suspend fun saveJsonObject(jsonString: String)
fun getJsonObject(): Flow<JsonObject?>
fun getJsonObjectAsString(): Flow<String?>
suspend fun clearJsonObject()

// JSON Array operations
suspend fun saveJsonArray(jsonArray: JsonArray)
suspend fun saveJsonArray(jsonString: String)
suspend fun saveJsonArray(list: List<Any>)
fun getJsonArray(): Flow<JsonArray?>
fun getJsonArrayAsString(): Flow<String?>
fun getJsonArrayAsList(): Flow<List<Any>?>
suspend fun addToJsonArray(item: Any)
suspend fun removeFromJsonArray(index: Int)
suspend fun clearJsonArray()
```

#### Proto DataStore Repository
```kotlin
// User settings
suspend fun updateUserName(name: String)
suspend fun updateUserAge(age: Int)
suspend fun updatePremiumStatus(isPremium: Boolean)
suspend fun updateEmail(email: String)

// Topics management
suspend fun addFavoriteTopic(topic: String)
suspend fun removeFavoriteTopic(topic: String)

// Theme settings
suspend fun updateTheme(primaryColor: String, isDarkMode: Boolean, fontSize: String)

// Notification settings
suspend fun updateNotificationSettings(email: Boolean, push: Boolean, sms: Boolean, frequency: Int)

// Data management
suspend fun clearAllData()
```

## 📋 Requirements

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Kotlin**: 2.0.21
- **Compose**: 2024.09.00
- **Protobuf**: 3.25.1

## 🛠️ Build Instructions

1. **Clone the repository**
2. **Open in Android Studio**
3. **Sync Gradle files** (this will generate protobuf classes)
4. **Run on device/emulator**

### 🔧 **Protobuf Generation**
The app uses the protobuf plugin to automatically generate Java classes from the `.proto` files. After syncing Gradle, you'll find generated classes in:
```
app/build/generated/source/proto/debug/java/com/naveen/jetpackdatastore/proto/
```

## 📝 Key Notes

### 🔐 **Encrypted Preferences DataStore**
- All data is automatically encrypted/decrypted transparently
- Repository can be injected anywhere in the app for data access
- StateFlows provide reactive UI updates
- Database name: `NaveenPrivateDataBase`
- Keyset name: `naveen_keyset`
- **JSON Support**: Complex JSON objects and arrays with Gson serialization and encryption
- **Array Management**: Dynamic add/remove operations for JSON arrays
- **Validation**: JSON format validation before saving
- **Smooth Scrolling**: Optimized UI with proper scrollable layout

### 📊 **Proto DataStore**
- Type-safe data storage with compile-time validation
- Schema evolution support for future updates
- Better performance with binary serialization
- File: `user_settings.pb`
- Generated classes provide builder pattern and immutable objects

## 🎯 **When to Use Which DataStore**

### Use **Preferences DataStore** when:
- ✅ Simple key-value pairs
- ✅ Need encryption at rest
- ✅ Quick setup and implementation
- ✅ Basic data types (String, Int, Boolean, etc.)
- ✅ JSON objects and arrays with encryption
- ✅ Dynamic array manipulation (add/remove items)
- ✅ Flexible data structures

### Use **Proto DataStore** when:
- ✅ Complex data structures
- ✅ Type safety is critical
- ✅ Need schema evolution
- ✅ Better performance requirements
- ✅ Nested objects and repeated fields