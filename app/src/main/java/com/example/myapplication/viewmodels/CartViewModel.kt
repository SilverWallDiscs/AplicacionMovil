package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.CartPreferences
import com.example.myapplication.screens.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartPrefs = CartPreferences(application) // instancia de preferencias del carrito

    // estado interno del carrito (lista de pares: producto + cantidad)
    private val _cartItems = MutableStateFlow<MutableList<Pair<Product, Int>>>(mutableListOf())
    val cartItems: StateFlow<MutableList<Pair<Product, Int>>> = _cartItems // exposicion del estado

    // carga el carrito de un usuario desde DataStore
    fun loadCart(userName: String, productsList: List<Product>) {
        viewModelScope.launch {
            val loaded = cartPrefs.getCart(userName, productsList)
            _cartItems.value = loaded.toMutableList()
        }
    }

    // agrega un producto al carrito o aumenta la cantidad si ya existe
    fun addToCart(product: Product, userName: String) {
        viewModelScope.launch {
            val list = _cartItems.value.toMutableList()
            val index = list.indexOfFirst { it.first.id == product.id }
            if (index >= 0) {
                val current = list[index]
                list[index] = current.copy(second = current.second + 1)
            } else {
                list.add(product to 1)
            }
            _cartItems.value = list
            cartPrefs.saveCart(userName, list)
        }
    }

    // reduce la cantidad de un producto en el carrito, si es 1 no lo quita
    fun removeFromCart(product: Product, userName: String) {
        viewModelScope.launch {
            val list = _cartItems.value.toMutableList()
            val index = list.indexOfFirst { it.first.id == product.id }
            if (index >= 0) {
                val current = list[index]
                if (current.second > 1) {
                    list[index] = current.copy(second = current.second - 1)
                }
            }
            _cartItems.value = list
            cartPrefs.saveCart(userName, list)
        }
    }

    // elimina completamente un producto del carrito
    fun clearCartItem(product: Product, userName: String) {
        viewModelScope.launch {
            val list = _cartItems.value.toMutableList()
            list.removeAll { it.first.id == product.id }
            _cartItems.value = list
            cartPrefs.saveCart(userName, list)
        }
    }

    // vacía todo el carrito
    fun clearCart(userName: String) {
        viewModelScope.launch {
            _cartItems.value = mutableListOf()
            cartPrefs.clearCart(userName)
        }
    }

    // calcula el total del carrito
    fun getTotal(): Double {
        return _cartItems.value.sumOf { it.first.price * it.second }
    }

    // ----------------- FUNCIONES REPORTES -----------------

    // guarda un reporte de compra y vacía el carrito
    fun purchaseCart(userName: String) {
        viewModelScope.launch {
            cartPrefs.addPurchaseReport(userName, _cartItems.value)
            clearCart(userName)
        }
    }

    // devuelve los reportes de compras en un StateFlow
    fun getReports(): StateFlow<List<String>> {
        val flow = MutableStateFlow<List<String>>(emptyList())
        viewModelScope.launch { flow.value = cartPrefs.getAllReports() }
        return flow
    }

    // guarda un reporte de la compra actual sin vaciar el carrito
    fun savePurchaseReport(userName: String) {
        viewModelScope.launch {
            val currentCart = cartItems.value
            cartPrefs.addPurchaseReport(userName, currentCart)
        }
    }
}