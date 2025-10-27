package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.navigation.Screen
import com.example.myapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, userViewModel: UserViewModel) {
    // observo el nombre del usuario desde el viewmodel
    val userNameState = userViewModel.userName.collectAsState(initial = null)
    val userName = userNameState.value

    // estructura principal con barra superior
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // titulo centrado de la app
                    Text(
                        text = "Tienda Y2K",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        // contenido principal de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // logo principal
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Tienda Y2K",
                modifier = Modifier.size(100.dp).padding(bottom = 24.dp)
            )

            // texto de bienvenida
            Text(
                text = "Bienvenido a Tienda Y2K",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // texto secundario con descripcion
            Text(
                text = "Tu destino favorito para lo retro-futurista ✨",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // columna con botones principales
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // boton para ver los productos
                Button(
                    onClick = { navController.navigate(Screen.Productos.route) },
                    modifier = Modifier.fillMaxWidth(0.85f).height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Ver Productos")
                }

                // boton para entrar como admin
                Button(
                    onClick = { navController.navigate(Screen.AdminLogin.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Admin Login")
                }

                // boton para ver el carrito
                OutlinedButton(
                    onClick = { navController.navigate(Screen.Carrito.route) },
                    modifier = Modifier.fillMaxWidth(0.85f).height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) { Text("Mi Carrito") }

                // si no hay usuario muestra opciones de login y registro
                if (userName == null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        TextButton(
                            onClick = { navController.navigate(Screen.Login.route) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Iniciar Sesion") }

                        TextButton(
                            onClick = { navController.navigate(Screen.Register.route) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Registrarse") }
                    }
                } else {
                    // si hay usuario muestra saludo y boton de cerrar sesion
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Hola, $userName", style = MaterialTheme.typography.titleMedium)
                        Button(onClick = { userViewModel.logout() }) {
                            Text("Cerrar Sesion")
                        }
                    }
                }
            }
        }
    }
}