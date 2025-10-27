package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.*
import com.example.myapplication.viewmodel.CartViewModel
import com.example.myapplication.viewmodel.ProductViewModel
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(), // controlador de navegacion
    userViewModel: UserViewModel, // viewmodel del usuario
    cartViewModel: CartViewModel, // viewmodel del carrito
    productsList: List<Product> // lista de productos
) {
    // definimos las rutas principales de la app con su destino inicial
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        // pantalla principal
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, userViewModel = userViewModel)
        }

        // pantalla de productos
        composable(Screen.Productos.route) {
            val productViewModel: ProductViewModel = viewModel()
            ProductosScreen(
                navController = navController,
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        // pantalla del carrito de compras
        composable(Screen.Carrito.route) {
            val productViewModel: ProductViewModel = viewModel()
            CarritoScreen(
                navController = navController,
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                productViewModel = productViewModel // pasamos el viewmodel de productos
            )
        }

        // pantalla de inicio de sesion de usuario
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, userViewModel = userViewModel)
        }

        // pantalla de registro
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, userViewModel = userViewModel)
        }

        // login del administrador
        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(navController = navController)
        }

        // panel principal del administrador
        composable(Screen.Admin.route) {
            AdminPanelScreen(navController = navController)
        }

        // pantalla para gestionar productos
        composable(Screen.GestionarProductos.route) {
            val productViewModel: ProductViewModel = viewModel()
            GestionarProductosScreen(
                navController = navController,
                productViewModel = productViewModel
            )
        }

        // pantalla para gestionar usuarios
        composable(Screen.GestionarUsuarios.route) {
            GestionarUsuariosScreen(navController = navController, userViewModel = userViewModel)
        }

        // pantalla de reportes del admin
        composable(Screen.VerReportes.route) {
            VerReportesScreen(navController = navController, cartViewModel = cartViewModel)
        }

        // pantalla de contacto
        composable(Screen.Contacto.route) {
            ContactoScreen(navController = navController)
        }
    }
}