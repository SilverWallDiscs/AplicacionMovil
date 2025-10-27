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
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel,
    productsList: List<Product>
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(navController = navController, userViewModel = userViewModel)
        }

        composable(Screen.Productos.route) {
            val productViewModel: ProductViewModel = viewModel()
            ProductosScreen(
                navController = navController,
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        composable(Screen.Carrito.route) {
            val productViewModel: ProductViewModel = viewModel()
            CarritoScreen(
                navController = navController,
                userViewModel = userViewModel,
                cartViewModel = cartViewModel,
                productViewModel = productViewModel // <- aquí pasamos el ViewModel
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController, userViewModel = userViewModel)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, userViewModel = userViewModel)
        }

        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(navController = navController)
        }

        composable(Screen.Admin.route) {
            AdminPanelScreen(navController = navController)
        }

        // Pantallas funcionales de administración
        composable(Screen.GestionarProductos.route) {
            val productViewModel: ProductViewModel = viewModel()
            GestionarProductosScreen(
                navController = navController,
                productViewModel = productViewModel
            )
        }

        composable(Screen.GestionarUsuarios.route) {
            GestionarUsuariosScreen(navController = navController, userViewModel = userViewModel)
        }

        composable(Screen.VerReportes.route) {
            VerReportesScreen(navController = navController, cartViewModel = cartViewModel)
        }

        composable(Screen.Contacto.route) {
            ContactoScreen(navController = navController)
        }
    }
}
