package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ProductPreferences
import com.example.myapplication.screens.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = ProductPreferences(application)

    private val _products = MutableStateFlow<List<Product>>(emptyList()) // lista inmutable
    val products: StateFlow<List<Product>> = _products

    init {
        viewModelScope.launch {
            _products.value = prefs.getProducts()
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            val updatedList = _products.value + product
            _products.value = updatedList
            prefs.saveProducts(updatedList.toMutableList()) // prefs usa MutableList
        }
    }

    fun updateProduct(updated: Product) {
        viewModelScope.launch {
            val updatedList = _products.value.map {
                if (it.id == updated.id) updated else it
            }
            _products.value = updatedList
            prefs.saveProducts(updatedList.toMutableList())
        }
    }

    fun removeProduct(product: Product) {
        viewModelScope.launch {
            val updatedList = _products.value.filter { it.id != product.id }
            _products.value = updatedList
            prefs.saveProducts(updatedList.toMutableList())
        }
    }
}
