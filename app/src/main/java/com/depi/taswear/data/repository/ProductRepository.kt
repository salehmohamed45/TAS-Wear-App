package com.depi.taswear.data.repository

import com.depi.taswear.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/**
 * Repository for handling product operations using Firestore
 */
class ProductRepository(
    private val firestore: FirebaseFirestore
) {
    
    private val productsCollection = firestore.collection("products")
    
    /**
     * Get all products
     */
    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val snapshot = productsCollection.get().await()
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get products by category
     */
    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val snapshot = productsCollection
                .whereEqualTo("category", category)
                .get()
                .await()
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Search products by name
     * Note: For better performance with large catalogs, consider implementing:
     * - Firestore composite queries with indexes
     * - Algolia or ElasticSearch for full-text search
     * - Server-side filtering with Cloud Functions
     * Current implementation fetches all products and filters client-side,
     * which is suitable for small to medium catalogs (< 1000 products)
     */
    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val snapshot = productsCollection.get().await()
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }.filter { 
                it.name.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.sku.contains(query, ignoreCase = true)
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get product by ID
     */
    suspend fun getProductById(productId: String): Result<Product> {
        return try {
            val doc = productsCollection.document(productId).get().await()
            val product = doc.toObject(Product::class.java)?.copy(id = doc.id)
            
            if (product != null) {
                Result.success(product)
            } else {
                Result.failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get featured product (Meal of the Day / Featured Look)
     */
    suspend fun getFeaturedProduct(): Result<Product?> {
        return try {
            val snapshot = productsCollection
                .whereEqualTo("isFeatured", true)
                .limit(1)
                .get()
                .await()
            
            val product = snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Add new product (Admin only)
     */
    suspend fun addProduct(product: Product): Result<String> {
        return try {
            val docRef = productsCollection.add(product).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Update existing product (Admin only)
     */
    suspend fun updateProduct(productId: String, product: Product): Result<Unit> {
        return try {
            productsCollection.document(productId).set(product).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Delete product (Admin only)
     */
    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            productsCollection.document(productId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
