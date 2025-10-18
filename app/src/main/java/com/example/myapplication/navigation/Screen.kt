package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Productos : Screen("productos")
    object Carrito : Screen("carrito")
    object Login : Screen("login")
    object Register : Screen("register")
    object Admin : Screen("admin")
    object Contacto : Screen("contacto")
    object AdminLogin : Screen("admin_login")
}