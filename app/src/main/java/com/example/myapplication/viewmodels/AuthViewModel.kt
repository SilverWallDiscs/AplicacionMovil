package com.example.myapplication.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class User(val nombre: String, val apellido: String, val username: String, val password: String)

class AuthViewModel(private val context: Context? = null) : ViewModel() {
    val isLoggedIn = mutableStateOf(false)
    val currentUser = mutableStateOf<User?>(null)

    fun login(username: String, password: String): Boolean {
        val users = getUsers()
        val user = users.find { it.username == username && it.password == password }
        if (user != null) {
            isLoggedIn.value = true
            currentUser.value = user
            return true
        }
        return false
    }

    fun register(user: User): Boolean {
        val users = getUsers().toMutableList()
        if (users.any { it.username == user.username }) return false
        users.add(user)
        saveUsers(users)
        return true
    }

    fun logout() {
        isLoggedIn.value = false
        currentUser.value = null
    }

    private fun getUsers(): List<User> {
        val prefs = context?.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val json = prefs?.getString("users", "[]") ?: "[]"
        val type = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun saveUsers(users: List<User>) {
        val prefs = context?.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val json = Gson().toJson(users)
        prefs?.edit()?.putString("users", json)?.apply()
    }
}