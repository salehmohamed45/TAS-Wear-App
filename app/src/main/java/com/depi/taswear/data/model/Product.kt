package com.depi.taswear.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrls: List<String> = emptyList(),
    val category: String = "",
    val brand: String = "",
    val sizes: List<String> = emptyList(),
    val colors: List<String> = emptyList(),
    val stock: Int = 0
)
