package com.example.myapplication.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.viewmodel.ProductViewModel

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUri: String
)

@Composable
fun GestionarProductosScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    // observo la lista de productos del viewmodel
    val products by productViewModel.products.collectAsState()

    // variables para agregar un nuevo producto
    var newName by remember { mutableStateOf("") }
    var newPrice by remember { mutableStateOf("") }
    var newImageUri by remember { mutableStateOf("") }

    // launcher para abrir la galeria y seleccionar imagen
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { newImageUri = it.toString() }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // titulo
        Text("Gestionar Productos", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        // campo nombre
        OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // campo precio
        OutlinedTextField(value = newPrice, onValueChange = { newPrice = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // boton para seleccionar imagen
        Button(onClick = { launcher.launch("image/*") }) {
            Text(if (newImageUri.isEmpty()) "Seleccionar imagen" else "Imagen seleccionada")
        }

        Spacer(Modifier.height(8.dp))

        // boton para agregar producto
        Button(onClick = {
            val price = newPrice.toDoubleOrNull() ?: 0.0
            if (newName.isNotBlank() && newImageUri.isNotBlank()) {
                val id = (products.maxOfOrNull { it.id } ?: 0) + 1
                val product = Product(id, newName, price, newImageUri)
                productViewModel.addProduct(product)
                // limpiar campos despues de agregar
                newName = ""
                newPrice = ""
                newImageUri = ""
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar Producto")
        }

        Spacer(Modifier.height(16.dp))

        // lista de productos agregados
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(products) { product ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        // imagen del producto
                        if (product.imageUri.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(product.imageUri),
                                contentDescription = product.name,
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // nombre y precio
                        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text("$${"%,.0f".format(product.price)}", style = MaterialTheme.typography.bodyMedium)
                        }

                        // boton eliminar
                        Column {
                            Button(onClick = { productViewModel.removeProduct(product) }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // boton volver atras
        Button(onClick = { navController.navigateUp() }, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}