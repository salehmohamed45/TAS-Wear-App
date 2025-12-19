package com.depi.taswear.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.depi.taswear.data.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for managing shopping cart
 */
class CartViewModel : ViewModel() {
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()
    
    /**
     * Add item to cart
     */
    fun addToCart(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.productId == item.productId }
        
        if (existingItem != null) {
            // Update quantity if item already exists
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(quantity = existingItem.quantity + item.quantity)
        } else {
            // Add new item
            currentItems.add(item)
        }
        
        _cartItems.value = currentItems
        calculateTotal()
    }
    
    /**
     * Remove item from cart
     */
    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.productId != productId }
        calculateTotal()
    }
    
    /**
     * Update item quantity
     */
    fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        
        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.productId == productId }
        
        if (itemIndex != -1) {
            currentItems[itemIndex] = currentItems[itemIndex].copy(quantity = quantity)
            _cartItems.value = currentItems
            calculateTotal()
        }
    }
    
    /**
     * Clear all items from cart
     */
    fun clearCart() {
        _cartItems.value = emptyList()
        _totalAmount.value = 0.0
    }
    
    /**
     * Calculate total amount
     */
    private fun calculateTotal() {
        val total = _cartItems.value.sumOf { it.productPrice * it.quantity }
        _totalAmount.value = total
    }
    
    /**
     * Get cart item count
     */
    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
}
