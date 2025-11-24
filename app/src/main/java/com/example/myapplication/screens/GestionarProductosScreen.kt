package com.example.myapplication.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.viewmodel.ProductViewModel

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUri: String
)

fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    throw IllegalStateException("No Activity found")
}

fun openAppSettings(context: Context) {
    val intent = android.content.Intent(
        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val filename = "camera_${System.currentTimeMillis()}.png"
    val file = java.io.File(context.cacheDir, filename)

    java.io.FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    return Uri.fromFile(file)
}

@Composable
fun GestionarProductosScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val products by productViewModel.products.collectAsState()

    var newName by remember { mutableStateOf("") }
    var newPrice by remember { mutableStateOf("") }
    var newImageUri by remember { mutableStateOf("") }

    val context = LocalContext.current

    // ---------- PERMISOS ----------
    val cameraPermission = android.Manifest.permission.CAMERA
    val galleryPermission =
        if (android.os.Build.VERSION.SDK_INT >= 33)
            android.Manifest.permission.READ_MEDIA_IMAGES
        else
            android.Manifest.permission.READ_EXTERNAL_STORAGE

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { (perm, granted) ->
            if (!granted) {
                Toast.makeText(context, "Permiso denegado: $perm", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            newImageUri = saveBitmapToCache(context, it).toString()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { newImageUri = it.toString() }
    }

    var showDialog by remember { mutableStateOf(false) }
    var showPermissionBlockedDialog by remember { mutableStateOf(false) }


    if (showPermissionBlockedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionBlockedDialog = false },
            title = { Text("Permisos necesarios") },
            text = { Text("Debes otorgar permisos a la aplicación para acceder a la cámara o galería.") },
            confirmButton = {
                Button(onClick = {
                    showPermissionBlockedDialog = false
                    openAppSettings(context)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showPermissionBlockedDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Seleccionar imagen") },
            text = { Text("¿Cómo quieres obtener la imagen?") },

            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    val act = context.findActivity()

                    if (ActivityCompat.checkSelfPermission(context, cameraPermission)
                        == android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraLauncher.launch(null)
                    } else {
                        // BLOQUEADO PERMANENTEMENTE
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(act, cameraPermission)) {
                            showPermissionBlockedDialog = true
                        } else {
                            permissionLauncher.launch(arrayOf(cameraPermission))
                        }
                    }
                }) {
                    Text("Usar cámara")
                }
            },

            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    val act = context.findActivity()

                    if (ActivityCompat.checkSelfPermission(context, galleryPermission)
                        == android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        galleryLauncher.launch("image/*")
                    } else {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(act, galleryPermission)) {
                            showPermissionBlockedDialog = true
                        } else {
                            permissionLauncher.launch(arrayOf(galleryPermission))
                        }
                    }
                }) {
                    Text("Elegir desde galería")
                }
            }
        )
    }


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Gestionar Productos", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = newPrice,
            onValueChange = { newPrice = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = { showDialog = true }) {
            Text(if (newImageUri.isEmpty()) "Seleccionar imagen" else "Imagen seleccionada")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                val price = newPrice.toDoubleOrNull() ?: 0.0
                if (newName.isNotBlank() && newImageUri.isNotBlank()) {
                    val id = (products.maxOfOrNull { it.id } ?: 0) + 1
                    productViewModel.addProduct(Product(id, newName, price, newImageUri))

                    newName = ""
                    newPrice = ""
                    newImageUri = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Producto")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(products) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        if (product.imageUri.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(product.imageUri),
                                contentDescription = product.name,
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f).padding(start = 12.dp)
                        ) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text("$${"%,.0f".format(product.price)}", style = MaterialTheme.typography.bodyMedium)
                        }

                        Button(onClick = { productViewModel.removeProduct(product) }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
