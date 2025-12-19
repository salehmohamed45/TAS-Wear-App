package com.depi.taswear.data.model

import com.google.firebase.Timestamp

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val imageUrls: List<String> = emptyList(),
    val category: String = "",
    val stock: Int = 0,
    val createdAt: Timestamp? = null,
    val isFeatured: Boolean = false
)
