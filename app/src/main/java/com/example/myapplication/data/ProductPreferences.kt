package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.screens.Product
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.productDataStore by preferencesDataStore("products_prefs")

class ProductPreferences(private val context: Context) {

    private val PRODUCTS_KEY = stringPreferencesKey("products")

    // Guardar lista de productos
    suspend fun saveProducts(products: List<Product>) {
        val serialized = products.joinToString(";") { "${it.id},${it.name},${it.price},${it.imageUri}" }
        context.productDataStore.edit { prefs ->
            prefs[PRODUCTS_KEY] = serialized
        }
    }

    // Cargar productos
    suspend fun getProducts(): MutableList<Product> {
        val data = context.productDataStore.data.map { it[PRODUCTS_KEY] ?: "" }.first()
        if (data.isBlank()) return mutableListOf()
        val list = mutableListOf<Product>()
        data.split(";").forEach { entry ->
            val parts = entry.split(",")
            if (parts.size == 4) {
                val id = parts[0].toIntOrNull() ?: return@forEach
                val name = parts[1]
                val price = parts[2].toDoubleOrNull() ?: 0.0
                val imageUri = parts[3]
                list.add(Product(id, name, price, imageUri))
            }
        }
        return list
    }
}
