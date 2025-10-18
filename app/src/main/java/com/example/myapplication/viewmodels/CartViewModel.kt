package com.example.myapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Product(val id: Int, val nombre: String, val precio: Int, val imagen: String, val categoria: String, val descripcion: String = "", val stock: Int = 0, var cantidad: Int = 1)

class CartViewModel : ViewModel() {
    val cart = mutableStateListOf<Product>()

    fun addToCart(product: Product) {
        val existing = cart.find { it.id == product.id }
        if (existing != null) {
            existing.cantidad++
        } else {
            cart.add(product.copy())
        }
    }

    fun removeFromCart(index: Int) {
        cart.removeAt(index)
    }

    fun clearCart() {
        cart.clear()
    }

    fun updateQuantity(index: Int, quantity: Int) {
        if (index in cart.indices) {
            cart[index].cantidad = quantity
        }
    }

    fun getTotal(): Int = cart.sumOf { it.precio * it.cantidad }
}