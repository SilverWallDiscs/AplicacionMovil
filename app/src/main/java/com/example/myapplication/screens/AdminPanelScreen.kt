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
// uso esta anotacion porque topappbar aun esta en experimental
@Composable
// esta funcion muestra el panel del admin
fun AdminPanelScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        // uso una columna para organizar todo en vertical

        TopAppBar(
            // barra superior con el titulo y el boton de volver
            title = { Text("Panel de Administracion") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    // boton para volver atras
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    // icono de flecha hacia atras
                }
            }
        )

        Column(
            // otra columna para el contenido principal
            modifier = Modifier
                .fillMaxSize()
                // ocupa toda la pantalla
                .padding(16.dp),
            // agrega un poco de espacio
            horizontalAlignment = Alignment.CenterHorizontally,
            // centro horizontal
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            // separa los botones con espacio entre ellos y centra vertical
        ) {
            Text(
                // titulo dentro del panel
                text = "Opciones de Administracion",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                // boton para ir a la gestion de productos
                onClick = { navController.navigate(Screen.GestionarProductos.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Productos")
            }

            Button(
                // boton para ir a la gestion de usuarios
                onClick = { navController.navigate(Screen.GestionarUsuarios.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Usuarios")
            }

            Button(
                // boton para ver los reportes
                onClick = { navController.navigate(Screen.VerReportes.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Reportes")
            }

        }
    }
}
