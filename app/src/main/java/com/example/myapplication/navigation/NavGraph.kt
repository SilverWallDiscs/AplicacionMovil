package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.*

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Productos.route) {
            ProductosScreen(navController = navController)
        }
        composable(Screen.Carrito.route) {
            CarritoScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.Admin.route) {
            AdminPanelScreen(navController = navController)
        }
        composable(Screen.Contacto.route) {

            ContactoScreen(navController = navController)
        }
        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(navController = navController)
        }
    }
}