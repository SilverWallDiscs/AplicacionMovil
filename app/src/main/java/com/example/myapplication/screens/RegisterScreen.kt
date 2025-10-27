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
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current

    // variables para guardar los datos del formulario
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // estructura principal centrada
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // campos de texto para registrar usuario
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        )

        // boton principal para registrar
        Button(
            onClick = {
                when {
                    // validaciones basicas del formulario
                    name.isBlank() || email.isBlank() || password.isBlank() -> {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                    password != confirmPassword -> {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // si todo esta bien se llama al viewmodel para registrar
                        userViewModel.register(name, email, password) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // boton para ir al login si ya tiene cuenta
        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }

        // boton para volver atras
        TextButton(onClick = { navController.navigateUp() }) {
            Text("Volver")
        }
    }
}