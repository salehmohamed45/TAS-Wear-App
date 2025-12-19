package com.depi.taswear.data.model

data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val productImage: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val selectedSize: String = "",
    val selectedColor: String = ""
)
