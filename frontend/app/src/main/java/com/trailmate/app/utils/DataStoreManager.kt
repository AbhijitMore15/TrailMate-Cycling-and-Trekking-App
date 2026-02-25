package com.trailmate.app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import com.trailmate.app.models.User

// ================= DATASTORE EXTENSION =================
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "trailmate_prefs"
)

class DataStoreManager(private val context: Context) {

    companion object {

        // AUTH
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val FITNESS_LEVEL_KEY = stringPreferencesKey("fitness_level")

        // PROFILE
        private val HEIGHT_KEY = stringPreferencesKey("height")
        private val WEIGHT_KEY = stringPreferencesKey("weight")
        private val GOAL_KEY = stringPreferencesKey("goal")

        // NOTES (NEW)
        private val NOTES_KEY = stringPreferencesKey("daily_notes")
    }

    // =====================================================
    // AUTH SECTION
    // =====================================================

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { it[AUTH_TOKEN_KEY] = token }
    }

    val tokenFlow: Flow<String?> =
        context.dataStore.data.map { it[AUTH_TOKEN_KEY] }

    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { it[USER_ID_KEY] = userId }
    }

    val userIdFlow: Flow<Int?> =
        context.dataStore.data.map { it[USER_ID_KEY] }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { it[USER_EMAIL_KEY] = email }
    }

    val emailFlow: Flow<String?> =
        context.dataStore.data.map { it[USER_EMAIL_KEY] }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { it[USER_NAME_KEY] = name }
    }

    val userNameFlow: Flow<String?> =
        context.dataStore.data.map { it[USER_NAME_KEY] }

    suspend fun saveFitnessLevel(level: String) {
        context.dataStore.edit { it[FITNESS_LEVEL_KEY] = level }
    }

    val fitnessLevelFlow: Flow<String?> =
        context.dataStore.data.map { it[FITNESS_LEVEL_KEY] }

    // =====================================================
    // PROFILE SECTION
    // =====================================================

    suspend fun saveProfile(
        name: String,
        height: String,
        weight: String,
        goal: String
    ) {
        context.dataStore.edit {
            it[USER_NAME_KEY] = name
            it[HEIGHT_KEY] = height
            it[WEIGHT_KEY] = weight
            it[GOAL_KEY] = goal
        }
    }

    val profileFlow: Flow<User> =
        context.dataStore.data.map {
            User(
                name = it[USER_NAME_KEY] ?: "",
                height = it[HEIGHT_KEY] ?: "",
                weight = it[WEIGHT_KEY] ?: "",
                goal = it[GOAL_KEY] ?: ""
            )
        }

    // =====================================================
    // NOTES SECTION (NEW)
    // =====================================================

    suspend fun saveNote(date: String, note: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[NOTES_KEY] ?: ""

            val updated = current
                .split("|")
                .filter { it.isNotBlank() && !it.startsWith("$date=") }
                .toMutableList()

            if (note.isNotBlank()) {
                updated.add("$date=$note")
            }

            prefs[NOTES_KEY] = updated.joinToString("|")
        }
    }

    val notesFlow: Flow<Map<String, String>> =
        context.dataStore.data.map { prefs ->
            val raw = prefs[NOTES_KEY] ?: ""

            raw.split("|")
                .filter { it.contains("=") }
                .associate {
                    val parts = it.split("=")
                    parts[0] to parts[1]
                }
        }

    // =====================================================
    // SYSTEM SECTION
    // =====================================================

    suspend fun clearAllData() {
        context.dataStore.edit { it.clear() }
    }

    val isLoggedInFlow: Flow<Boolean> =
        context.dataStore.data.map { it[AUTH_TOKEN_KEY] != null }

    suspend fun getAuthTokenOnce(): String? {
        return tokenFlow.firstOrNull()
    }
}