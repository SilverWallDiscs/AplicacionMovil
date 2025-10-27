package com.example.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.viewmodel.CartViewModel
import com.example.myapplication.viewmodel.ProductViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.example.myapplication.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current
    val userName by userViewModel.userName.collectAsState()
    val products by productViewModel.products.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()

    LaunchedEffect(userName, products) {
        if (userName != null) {
            cartViewModel.loadCart(userName!!, products)
        }
    }

    if (userName == null) {
        LaunchedEffect(Unit) { navController.navigate(Screen.Login.route) }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Carrito de Compras") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { (product, qty) ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Imagen del producto
                                if (product.imageUri.isNotEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(product.imageUri),
                                        contentDescription = product.name,
                                        modifier = Modifier.size(80.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }

                                // Botones de cantidad y eliminar
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { if (qty > 1) cartViewModel.removeFromCart(product, userName!!) }
                                    ) { Text("-") }

                                    Text("$qty", modifier = Modifier.padding(horizontal = 8.dp))

                                    IconButton(
                                        onClick = { cartViewModel.addToCart(product, userName!!) }
                                    ) { Text("+") }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { cartViewModel.clearCartItem(product, userName!!) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                        modifier = Modifier.height(36.dp)
                                    ) { Text("X", color = MaterialTheme.colorScheme.onError) }
                                }
                            }

                            // Nombre y precio debajo de todo
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(product.name, style = MaterialTheme.typography.titleMedium)
                                Text("$${product.price} x $qty", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // Total y botón de compra
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Total: $${cartViewModel.getTotal()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Guardar reporte en DataStore
                            cartViewModel.savePurchaseReport(userName!!)

                            // Limpiar carrito
                            cartViewModel.clearCart(userName!!)

                            Toast.makeText(context, "Compra registrada correctamente", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Comprar") }
                }
            }
        }
    }
}
