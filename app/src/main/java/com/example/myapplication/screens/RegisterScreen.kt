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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import android.util.Patterns
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            Button(
                onClick = {
                    when {
                        name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            errorMessage = "Por favor completa todos los campos."
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            errorMessage = "Por favor ingresa un correo válido."
                        }
                        password.length < 6 -> {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres."
                        }
                        password != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden."
                        }
                        else -> {
                            // Si pasa todas las validaciones, navega
                            navController.navigate(Screen.Productos.route)
                            return@Button
                        }
                    }

                    // Mostrar el error en un Snackbar
                    scope.launch {
                        snackbarHostState.showSnackbar(errorMessage)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }

            TextButton(onClick = { navController.navigateUp() }) {
                Text("Volver")
            }
        }
    }
}