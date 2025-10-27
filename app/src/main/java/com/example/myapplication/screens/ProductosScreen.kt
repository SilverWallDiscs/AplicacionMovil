package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import com.example.myapplication.navigation.Screen
import com.example.myapplication.viewmodel.CartViewModel
import com.example.myapplication.viewmodel.ProductViewModel
import com.example.myapplication.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel
) {
    // obtiene el nombre del usuario y la lista de productos desde los viewmodels
    val userName by userViewModel.userName.collectAsState()
    val products by productViewModel.products.collectAsState()

    Scaffold(
        // barra superior con titulo del catalogo y boton de volver
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Catálogo Y2K",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        // boton flotante para ir al carrito o al login
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (userName != null)
                        navController.navigate(Screen.Carrito.route)
                    else
                        navController.navigate(Screen.Login.route)
                }
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Ir al carrito")
            }
        }
    ) { padding ->
        // lista de productos
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(products) { product ->
                // cada producto se muestra dentro de una card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // muestra la imagen del producto si existe
                        if (product.imageUri.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(product.imageUri),
                                contentDescription = product.name,
                                modifier = Modifier.size(120.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // muestra el nombre, precio y boton para agregar al carrito
                        Column(
                            modifier = Modifier
                                .weight(0.6f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    product.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    "$${"%,.0f".format(product.price)}",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // boton para agregar al carrito o ir al login
                            Button(
                                onClick = {
                                    if (userName != null)
                                        cartViewModel.addToCart(product, userName!!)
                                    else
                                        navController.navigate(Screen.Login.route)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Agregar al carrito")
                            }
                        }
                    }
                }
            }
        }
    }
}