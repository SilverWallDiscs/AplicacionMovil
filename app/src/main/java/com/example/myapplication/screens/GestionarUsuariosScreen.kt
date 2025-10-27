package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun GestionarUsuariosScreen(navController: NavController, userViewModel: UserViewModel) {
    // observo los usuarios desde el viewmodel usando collectAsState
    val users by userViewModel.getAllUsers().collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // titulo de la pantalla
        Text("Gestionar Usuarios", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // lista con los usuarios
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(users) { user ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // muestra el nombre del usuario
                    Text(user)
                    // boton para resetear contrasena
                    Button(onClick = { userViewModel.updatePassword(user, "123456") }) {
                        Text("Reset Password")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // boton para volver atras
        Button(onClick = { navController.navigateUp() }) {
            Text("Volver")
        }
    }
}