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
// esta funcion dibuja la pantalla del carrito
fun CarritoScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current
    // obtengo el contexto para usar con toast
    val userName by userViewModel.userName.collectAsState()
    // guardo el nombre del usuario actual
    val products by productViewModel.products.collectAsState()
    // guardo la lista de productos
    val cartItems by cartViewModel.cartItems.collectAsState()
    // guardo los productos que estan en el carrito

    LaunchedEffect(userName, products) {
        // cuando cambia el usuario o los productos recargo el carrito
        if (userName != null) {
            cartViewModel.loadCart(userName!!, products)
        }
    }

    if (userName == null) {
        // si no hay usuario logueado vuelvo al login
        LaunchedEffect(Unit) { navController.navigate(Screen.Login.route) }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // estructura principal en columna
        TopAppBar(
            // barra superior con titulo y boton volver
            title = { Text("Carrito de Compras") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        if (cartItems.isEmpty()) {
            // si el carrito esta vacio muestro mensaje
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito esta vacio", style = MaterialTheme.typography.headlineSmall)
            }
        } else {
            // si hay productos muestro la lista
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { (product, qty) ->
                    // cada item del carrito
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
                                // imagen del producto
                                if (product.imageUri.isNotEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(product.imageUri),
                                        contentDescription = product.name,
                                        modifier = Modifier.size(80.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }

                                // botones de cantidad y eliminar
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { if (qty > 1) cartViewModel.removeFromCart(product, userName!!) }
                                    ) { Text("-") }
                                    // boton para restar cantidad

                                    Text("$qty", modifier = Modifier.padding(horizontal = 8.dp))
                                    // cantidad actual

                                    IconButton(
                                        onClick = { cartViewModel.addToCart(product, userName!!) }
                                    ) { Text("+") }
                                    // boton para sumar cantidad

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { cartViewModel.clearCartItem(product, userName!!) },
                                        // boton para eliminar producto del carrito
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                        modifier = Modifier.height(36.dp)
                                    ) { Text("X", color = MaterialTheme.colorScheme.onError) }
                                }
                            }

                            // nombre y precio debajo
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(product.name, style = MaterialTheme.typography.titleMedium)
                                Text("$${product.price} x $qty", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // total y boton de compra
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Total: $${cartViewModel.getTotal()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // guardo reporte en datastore
                            cartViewModel.savePurchaseReport(userName!!)

                            // limpio el carrito despues de comprar
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