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
import androidx.compose.material.icons.filled.ShoppingCart
@OptIn(ExperimentalMaterial3Api::class)

@Composable
// esta funcion dibuja la pantalla de contacto
fun ContactoScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    // guarda el nombre que escribe el usuario
    var email by remember { mutableStateOf("") }
    // guarda el correo que escribe el usuario
    var message by remember { mutableStateOf("") }
    // guarda el mensaje que escribe el usuario

    Column(modifier = Modifier.fillMaxSize()) {
        // estructura principal en columna
        TopAppBar(
            // barra superior con titulo y boton para volver atras
            title = { Text("Contacto") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        Column(
            // contenido principal de la pantalla
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // todo centrado horizontal
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
            // separo los elementos y los dejo arriba
        ) {
            Text(
                // titulo principal
                text = "Contactanos",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                // campo de texto para el nombre
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                // campo de texto para el correo
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                // campo de texto para el mensaje
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Button(
                // boton para enviar el mensaje
                onClick = { /* enviar mensaje */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Mensaje")
            }

            Spacer(modifier = Modifier.height(32.dp))
            // espacio para separar secciones

            Text(
                // titulo de la seccion de info
                text = "Informacion de Contacto",
                style = MaterialTheme.typography.titleMedium
            )

            // datos de contacto de la empresa
            Text("Email: contacto@y2k.com")
            Text("Telefono: +56 9 1234 5678")
            Text("Direccion: Calle duoc 123")
        }
    }
}