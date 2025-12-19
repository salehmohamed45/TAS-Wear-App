package com.depi.taswear.data.model

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: String = "pending", // "pending", "processing", "completed", "cancelled"
    val createdAt: Long = System.currentTimeMillis()
)
