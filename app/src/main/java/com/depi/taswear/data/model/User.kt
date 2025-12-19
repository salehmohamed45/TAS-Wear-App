package com.depi.taswear.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    CUSTOMER, ADMIN
}
