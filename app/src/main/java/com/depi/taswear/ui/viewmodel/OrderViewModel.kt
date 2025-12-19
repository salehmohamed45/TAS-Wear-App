package com.depi.taswear.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.taswear.data.model.Order
import com.depi.taswear.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling order operations
 */
class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    
    private val _ordersState = MutableStateFlow<OrdersState>(OrdersState.Initial)
    val ordersState: StateFlow<OrdersState> = _ordersState.asStateFlow()
    
    private val _userOrders = MutableStateFlow<List<Order>>(emptyList())
    val userOrders: StateFlow<List<Order>> = _userOrders.asStateFlow()
    
    private var currentUserId: String? = null
    
    /**
     * Create new order
     */
    fun createOrder(order: Order, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _ordersState.value = OrdersState.Loading
            val result = orderRepository.createOrder(order)
            
            _ordersState.value = if (result.isSuccess) {
                val orderId = result.getOrNull()!!
                onSuccess(orderId)
                OrdersState.OrderCreated(orderId)
            } else {
                OrdersState.Error(result.exceptionOrNull()?.message ?: "Failed to create order")
            }
        }
    }
    
    /**
     * Load user orders
     */
    fun loadUserOrders(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
            _ordersState.value = OrdersState.Loading
            val result = orderRepository.getUserOrders(userId)
            
            if (result.isSuccess) {
                _userOrders.value = result.getOrNull() ?: emptyList()
                _ordersState.value = OrdersState.Success
            } else {
                _ordersState.value = OrdersState.Error(
                    result.exceptionOrNull()?.message ?: "Failed to load orders"
                )
            }
        }
    }
    
    /**
     * Update order status
     */
    fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            val result = orderRepository.updateOrderStatus(orderId, status)
            if (result.isSuccess) {
                // Reload orders after update if we have a userId
                currentUserId?.let { userId ->
                    loadUserOrders(userId)
                }
            }
        }
    }
    
    /**
     * Reset order state
     */
    fun resetState() {
        _ordersState.value = OrdersState.Initial
    }
}

/**
 * Sealed class representing order states
 */
sealed class OrdersState {
    object Initial : OrdersState()
    object Loading : OrdersState()
    object Success : OrdersState()
    data class OrderCreated(val orderId: String) : OrdersState()
    data class Error(val message: String) : OrdersState()
}
