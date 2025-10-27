package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.screens.Product
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

// se crea el DataStore para guardar datos del carrito
val Context.cartDataStore by preferencesDataStore("cart_prefs")
val REPORTS_KEY = stringPreferencesKey("reports")

class CartPreferences(private val context: Context) {

    // genera una clave unica por usuario para su carrito
    private fun keyForUser(userName: String) = stringPreferencesKey("cart_$userName")

    // guarda el carrito de un usuario (producto + cantidad) como texto
    suspend fun saveCart(userName: String, cart: List<Pair<Product, Int>>) {
        val serialized = cart.joinToString(";") { "${it.first.id},${it.second}" }
        context.cartDataStore.edit { prefs ->
            prefs[keyForUser(userName)] = serialized
        }
    }

    // guarda un reporte de compra con fecha, usuario y total
    suspend fun addPurchaseReport(userName: String, cart: List<Pair<Product, Int>>) {
        val existingReports = context.cartDataStore.data.map { it[REPORTS_KEY] ?: "" }.first()

        val total = cart.sumOf { it.first.price * it.second } // total gastado
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val newReport = "$date | $userName compro ${cart.size} productos por $$total: " +
                cart.joinToString(", ") { "${it.first.name} x${it.second}" }

        val updated = if (existingReports.isBlank()) newReport else "$existingReports|$newReport"

        context.cartDataStore.edit { prefs ->
            prefs[REPORTS_KEY] = updated
        }
    }

    // obtiene todos los reportes guardados
    suspend fun getAllReports(): List<String> {
        val reports = context.cartDataStore.data.map { it[REPORTS_KEY] ?: "" }.first()
        if (reports.isBlank()) return emptyList()
        return reports.split("|")
    }

    // obtiene el carrito de un usuario a partir de los datos guardados
    suspend fun getCart(userName: String, productsList: List<Product>): MutableList<Pair<Product, Int>> {
        val data = context.cartDataStore.data.map { it[keyForUser(userName)] ?: "" }.first()
        if (data.isBlank()) return mutableListOf()

        val cart = mutableListOf<Pair<Product, Int>>()
        data.split(";").forEach { entry ->
            val parts = entry.split(",")
            if (parts.size == 2) {
                val productId = parts[0].toIntOrNull() ?: return@forEach
                val qty = parts[1].toIntOrNull() ?: return@forEach
                val product = productsList.firstOrNull { it.id == productId } ?: return@forEach
                cart.add(product to qty)
            }
        }
        return cart
    }

    // limpia el carrito del usuario
    suspend fun clearCart(userName: String) {
        context.cartDataStore.edit { prefs ->
            prefs.remove(keyForUser(userName))
        }
    }
}