package com.depi.taswear.data.model

data class Product(
    val id: String = "",
    val sku: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stockQty: Int = 0,
    val category: String = "",
    val imageUrl: String = "",
    val isFeatured: Boolean = false
)
