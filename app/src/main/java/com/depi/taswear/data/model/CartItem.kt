package com.depi.taswear.data.model

data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val productPrice: Double = 0.0,
    val quantity: Int = 1,
    val imageUrl: String = ""
)
