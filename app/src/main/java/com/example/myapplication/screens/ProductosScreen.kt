package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import java.text.DecimalFormat

data class Product(val id: Int, val name: String, val price: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(navController: NavController) {
    val products = remember {
        listOf(
            Product(1, "Polera Y2K", 10000.0),
            Product(2, "Jeans True Religion", 25000.0),
            Product(3, "Poleron Oversize", 20000.0)
        )
    }

    // Usamos DecimalFormat para formatear el precio sin los ceros innecesarios
    val decimalFormat = remember { DecimalFormat("#,###.##") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Productos") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* Acción al seleccionar producto */ }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text("$${decimalFormat.format(product.price)}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Button(onClick = { /* Agregar al carrito */ }) {
                            Text("Agregar")
                        }
                    }
                }
            }
        }

        // Botón flotante para ir al carrito
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Carrito.route) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
            }
        }
    }
}
