package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Extensión para DataStore
val Context.dataStore by preferencesDataStore(name = "users_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val USERS_KEY = stringPreferencesKey("users_map")
        private val CURRENT_USER_KEY = stringPreferencesKey("current_user")
    }

    val currentUser = context.dataStore.data.map { it[CURRENT_USER_KEY] ?: "" }

    suspend fun saveUser(email: String, password: String, name: String): Boolean {
        val existing = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        val usersList = existing.split(";").filter { it.isNotBlank() }
        if (usersList.any { it.split(":")[0] == email }) return false
        val newUser = "$email:$password:$name"
        context.dataStore.edit { prefs ->
            prefs[USERS_KEY] = if (existing.isBlank()) newUser else "$existing;$newUser"
        }
        return true
    }

    suspend fun login(email: String, password: String): String? {
        val users = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        val matched = users.split(";").mapNotNull {
            val parts = it.split(":")
            if (parts.size == 3) parts else null
        }.firstOrNull { it[0] == email && it[1] == password }

        matched?.let {
            context.dataStore.edit { prefs -> prefs[CURRENT_USER_KEY] = it[2] }
            return it[2]
        }

        return null
    }

    suspend fun logout() {
        context.dataStore.edit { prefs -> prefs[CURRENT_USER_KEY] = "" }
    }

    suspend fun getCurrentUser(): String? {
        val name = context.dataStore.data.map { it[CURRENT_USER_KEY] ?: "" }.first()
        return if (name.isBlank()) null else name
    }

    suspend fun emailExists(email: String): Boolean {
        val users = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        return users.split(";").any { it.split(":")[0] == email }
    }

    // ---------------- ADMIN ----------------

    suspend fun getAllUsersEmails(): List<String> {
        val users = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        return users.split(";").mapNotNull {
            val parts = it.split(":")
            if (parts.size == 3) parts[0] else null
        }
    }

    suspend fun updatePassword(email: String, newPassword: String) {
        val users = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        val updated = users.split(";").mapNotNull {
            val parts = it.split(":")
            if (parts.size == 3) {
                if (parts[0] == email) "${parts[0]}:$newPassword:${parts[2]}" else it
            } else null
        }.joinToString(";")
        context.dataStore.edit { prefs -> prefs[USERS_KEY] = updated }
    }
}
