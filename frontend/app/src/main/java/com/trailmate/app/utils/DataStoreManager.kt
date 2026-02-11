package com.trailmate.app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to access DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trailmate_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val FITNESS_LEVEL_KEY = stringPreferencesKey("fitness_level")
    }

    // Save authentication token
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    // Get authentication token
    fun getAuthToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN_KEY]
    }

    // Save user ID
    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // Get user ID
    fun getUserId(): Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    // Save user email
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }

    // Get user email
    fun getUserEmail(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY]
    }

    // Save user name
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    // Get user name
    fun getUserName(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY]
    }

    // Save fitness level
    suspend fun saveFitnessLevel(level: String) {
        context.dataStore.edit { preferences ->
            preferences[FITNESS_LEVEL_KEY] = level
        }
    }

    // Get fitness level
    fun getFitnessLevel(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[FITNESS_LEVEL_KEY]
    }

    // Clear all user data (on logout)
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Check if user is logged in
    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN_KEY] != null
    }
}