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
    val userNameState = userViewModel.userName.collectAsState(initial = null)
    val userName = userNameState.value


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Tienda Y2K",
                modifier = Modifier.size(100.dp).padding(bottom = 24.dp)
            )

            Text(
                text = "Bienvenido a Tienda Y2K",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Tu destino favorito para lo retro-futurista ✨",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = { navController.navigate(Screen.Productos.route) },
                    modifier = Modifier.fillMaxWidth(0.85f).height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Ver Productos")
                }

                Button(
                    onClick = { navController.navigate(Screen.AdminLogin.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Admin Login")
                }

                OutlinedButton(
                    onClick = { navController.navigate(Screen.Carrito.route) },
                    modifier = Modifier.fillMaxWidth(0.85f).height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) { Text("Mi Carrito") }

                if (userName == null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        TextButton(
                            onClick = { navController.navigate(Screen.Login.route) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Iniciar Sesión") }

                        TextButton(
                            onClick = { navController.navigate(Screen.Register.route) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Registrarse") }
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Hola, $userName", style = MaterialTheme.typography.titleMedium)
                        Button(onClick = { userViewModel.logout() }) {
                            Text("Cerrar Sesión")
                        }
                    }
                }
            }
        }
    }
}
