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
    val products by productViewModel.products.collectAsState()
    var newName by remember { mutableStateOf("") }
    var newPrice by remember { mutableStateOf("") }
    var newImageUri by remember { mutableStateOf("") }

    // Launcher para seleccionar imagen desde galería
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { newImageUri = it.toString() }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestionar Productos", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = newPrice, onValueChange = { newPrice = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick = { launcher.launch("image/*") }) {
            Text(if (newImageUri.isEmpty()) "Seleccionar imagen" else "Imagen seleccionada")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            val price = newPrice.toDoubleOrNull() ?: 0.0
            if (newName.isNotBlank() && newImageUri.isNotBlank()) {
                val id = (products.maxOfOrNull { it.id } ?: 0) + 1
                val product = Product(id, newName, price, newImageUri)
                productViewModel.addProduct(product)
                newName = ""
                newPrice = ""
                newImageUri = ""
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar Producto")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(products) { product ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        if (product.imageUri.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(product.imageUri),
                                contentDescription = product.name,
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text("$${"%,.0f".format(product.price)}", style = MaterialTheme.typography.bodyMedium)
                        }

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
        Button(onClick = { navController.navigateUp() }, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
