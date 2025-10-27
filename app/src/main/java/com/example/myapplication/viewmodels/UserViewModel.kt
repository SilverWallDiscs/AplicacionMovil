package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application) // instancia de UserPreferences (DataStore)

    // Estado interno del nombre del usuario actual
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> get() = _userName

    // al iniciar, cargamos el usuario actualmente logueado
    init {
        viewModelScope.launch {
            _userName.value = prefs.getCurrentUser()
        }
    }

    // ----------------- LOGIN / REGISTER -----------------

    // registrar un nuevo usuario
    fun register(name: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (prefs.emailExists(email)) {
                onResult(false, "El correo ya está registrado")
            } else {
                prefs.saveUser(email, password, name)
                _userName.value = name
                onResult(true, "Usuario registrado")
            }
        }
    }

    // login de usuario
    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val name = prefs.login(email, password)
            if (name != null) {
                _userName.value = name
                onResult(true, "Login exitoso")
            } else {
                onResult(false, "Correo o contraseña incorrectos")
            }
        }
    }

    // cerrar sesión
    fun logout() {
        viewModelScope.launch {
            prefs.logout()
            _userName.value = null
        }
    }

    // ----------------- FUNCIONES ADMIN -----------------

    // obtener todos los emails de usuarios registrados
    fun getAllUsers(): StateFlow<List<String>> {
        val flow = MutableStateFlow<List<String>>(emptyList())
        viewModelScope.launch {
            flow.value = prefs.getAllUsersEmails()
        }
        return flow
    }

    // actualizar contraseña de un usuario específico
    fun updatePassword(email: String, newPassword: String) {
        viewModelScope.launch {
            prefs.updatePassword(email, newPassword)
        }
    }
}