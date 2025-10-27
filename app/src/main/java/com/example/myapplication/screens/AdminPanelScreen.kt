package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.myapplication.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Panel de Administración") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "Opciones de Administración",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                onClick = { navController.navigate(Screen.GestionarProductos.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Productos")
            }

            Button(
                onClick = { navController.navigate(Screen.GestionarUsuarios.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Usuarios")
            }

            Button(
                onClick = { navController.navigate(Screen.VerReportes.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Reportes")
            }

            Button(
                onClick = { /* Configuraciones sin función aún */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configuraciones")
            }
        }
    }
}
