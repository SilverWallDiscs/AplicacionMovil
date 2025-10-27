package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.navigation.Screen


@Composable
// esta funcion dibuja la pantalla de login del admin
fun AdminLoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    // guardo el usuario que escribe
    var password by remember { mutableStateOf("") }
    // guardo la contrasena que escribe

    Column(
        // uso un column para poner todo vertical
        modifier = Modifier
            .fillMaxSize()
            // que ocupe toda la pantalla
            .padding(16.dp),
        // con un poco de espacio alrededor
        horizontalAlignment = Alignment.CenterHorizontally,
        // todo centrado horizontal
        verticalArrangement = Arrangement.Center
        // todo centrado vertical
    ) {
        Text(
            // titulo de la pantalla
            text = "Login Administrador",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
            // separacion abajo
        )

        OutlinedTextField(
            // input para el usuario
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario Admin") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            // input para la contrasena
            value = password,
            onValueChange = { password = it },
            label = { Text("Contrasena") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        Button(
            // boton para entrar al panel
            onClick = { navController.navigate(Screen.Admin.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Acceder al Panel")
            // texto del boton
        }

        Spacer(modifier = Modifier.height(16.dp))
        // espacio entre botones

        TextButton(onClick = { navController.navigateUp() }) {
            // boton para volver atras
            Text("Volver")
        }
    }
}
