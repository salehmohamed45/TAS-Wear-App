package com.depi.taswear.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val role: String = "customer" // "customer" or "admin"
)
