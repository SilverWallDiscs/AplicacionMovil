package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// extension para acceder al datastore de usuarios desde cualquier context
val Context.dataStore by preferencesDataStore(name = "users_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        // key donde se guardan los usuarios en formato "email:password:nombre"
        private val USERS_KEY = stringPreferencesKey("users_map")
        // key para guardar el usuario logeado actualmente
        private val CURRENT_USER_KEY = stringPreferencesKey("current_user")
    }

    // flujo que siempre devuelve el usuario actual logeado
    val currentUser = context.dataStore.data.map { it[CURRENT_USER_KEY] ?: "" }

    // guarda un usuario nuevo, devuelve false si ya existe ese email
    suspend fun saveUser(email: String, password: String, name: String): Boolean {
        val existing = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        val usersList = existing.split(";").filter { it.isNotBlank() }
        if (usersList.any { it.split(":")[0] == email }) return false // si ya existe, retorna false
        val newUser = "$email:$password:$name"
        context.dataStore.edit { prefs ->
            prefs[USERS_KEY] = if (existing.isBlank()) newUser else "$existing;$newUser"
        }
        return true
    }

    // intenta logear con email y contraseña, devuelve el nombre si es correcto
    suspend fun login(email: String, password: String): String? {
        val users = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        val matched = users.split(";").mapNotNull {
            val parts = it.split(":")
            if (parts.size == 3) parts else null
        }.firstOrNull { it[0] == email && it[1] == password }

        matched?.let {
            // guarda el usuario logeado en CURRENT_USER_KEY
            context.dataStore.edit { prefs -> prefs[CURRENT_USER_KEY] = it[2] }
            return it[2]
        }

        return null
    }

    // cierra sesion borrando el usuario actual
    suspend fun logout() {
        context.dataStore.edit { prefs -> prefs[CURRENT_USER_KEY] = "" }
    }

    // devuelve el usuario logeado actualmente o null si no hay
    suspend fun getCurrentUser(): String? {
        val name = context.dataStore.data.map { it[CURRENT_USER_KEY] ?: "" }.first()
        return if (name.isBlank()) null else name
    }

    // verifica si un email ya existe en el sistema
    suspend fun emailExists(email: String): Boolean {
        val users = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        return users.split(";").any { it.split(":")[0] == email }
    }

    // ---------------- FUNCIONES ADMIN ----------------

    // obtiene todos los emails de los usuarios registrados
    suspend fun getAllUsersEmails(): List<String> {
        val users = context.dataStore.data.map { it[USERS_KEY] ?: "" }.first()
        return users.split(";").mapNotNull {
            val parts = it.split(":")
            if (parts.size == 3) parts[0] else null
        }
    }

    // actualiza la contraseña de un usuario dado su email
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