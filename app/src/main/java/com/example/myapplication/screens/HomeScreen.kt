package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título principal
        Text(
            text = "Bienvenido a Tienda Y2K",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Subtítulo
        Text(
            text = "Tu tienda Y2K Favorita",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Botones de navegación
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón Productos
            Button(
                onClick = { navController.navigate(Screen.Productos.route) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Ver Productos",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Botón Carrito
            OutlinedButton(
                onClick = { navController.navigate(Screen.Carrito.route) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Mi Carrito",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Botón Login
            TextButton(
                onClick = { navController.navigate(Screen.Login.route) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Botón Registro
            TextButton(
                onClick = { navController.navigate(Screen.Register.route) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Registrarse",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Botón Contacto
            TextButton(
                onClick = { navController.navigate(Screen.Contacto.route) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Contacto",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón Admin (solo si es necesario)
        OutlinedButton(
            onClick = { navController.navigate(Screen.AdminLogin.route) },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
        ) {
            Text(
                text = "Admin",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}