package com.example.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.navigation.Screen
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {
    // contexto para mostrar mensajes toast
    val context = LocalContext.current
    // variables para guardar email y contrasena del usuario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // estructura principal de la pantalla de login
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // titulo principal
        Text("Iniciar Sesion", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 32.dp))

        // campo para escribir el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // campo para escribir la contrasena con ocultamiento de texto
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contrasena") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        )

        // boton para iniciar sesion
        Button(
            onClick = {
                userViewModel.login(email, password) { success, message ->
                    // muestra mensaje con toast
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    // si inicia sesion correctamente navega a home
                    if (success) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Iniciar Sesion")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // boton para ir a la pantalla de registro
        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text("No tienes cuenta Registrate")
        }

        // boton para volver atras
        TextButton(onClick = { navController.navigateUp() }) {
            Text("Volver")
        }
    }
}