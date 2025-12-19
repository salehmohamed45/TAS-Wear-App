package com.depi.taswear.data.repository

import com.depi.taswear.data.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/**
 * Repository for handling order operations using Firestore
 */
class OrderRepository(
    private val firestore: FirebaseFirestore
) {
    
    private val ordersCollection = firestore.collection("orders")
    
    /**
     * Create a new order
     */
    suspend fun createOrder(order: Order): Result<String> {
        return try {
            val docRef = ordersCollection.add(order).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get orders for a specific user
     */
    suspend fun getUserOrders(userId: String): Result<List<Order>> {
        return try {
            val snapshot = ordersCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Order::class.java)?.copy(orderId = doc.id)
            }
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get order by ID
     */
    suspend fun getOrderById(orderId: String): Result<Order> {
        return try {
            val doc = ordersCollection.document(orderId).get().await()
            val order = doc.toObject(Order::class.java)?.copy(orderId = doc.id)
            
            if (order != null) {
                Result.success(order)
            } else {
                Result.failure(Exception("Order not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Update order status
     */
    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> {
        return try {
            ordersCollection.document(orderId)
                .update("status", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get all orders (Admin only)
     */
    suspend fun getAllOrders(): Result<List<Order>> {
        return try {
            val snapshot = ordersCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Order::class.java)?.copy(orderId = doc.id)
            }
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
