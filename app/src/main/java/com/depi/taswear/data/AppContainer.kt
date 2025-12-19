package com.depi.taswear.data

import com.depi.taswear.data.repository.AuthRepository
import com.depi.taswear.data.repository.ProductRepository
import com.depi.taswear.data.repository.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Manual Dependency Injection Container
 * Manages Firebase instances and Repositories without using Hilt/Dagger
 */
class AppContainer {
    // Firebase instances
    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    
    val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    
    // Repositories
    val authRepository: AuthRepository by lazy {
        AuthRepository(firebaseAuth, firebaseFirestore)
    }
    
    val productRepository: ProductRepository by lazy {
        ProductRepository(firebaseFirestore)
    }
    
    val orderRepository: OrderRepository by lazy {
        OrderRepository(firebaseFirestore)
    }
}
