package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
                onClick = { /* Gestionar productos */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Productos")
            }

            Button(
                onClick = { /* Gestionar usuarios */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Usuarios")
            }

            Button(
                onClick = { /* Ver reportes */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Reportes")
            }

            Button(
                onClick = { /* Configuraciones */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configuraciones")
            }
        }
    }
}