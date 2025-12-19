package com.depi.taswear.data.repository

import com.depi.taswear.data.model.Product
import com.depi.taswear.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val productsCollection = firestore.collection("products")

    companion object {
        private const val FEATURED_PRODUCTS_LIMIT = 10L
    }

    /**
     * Fetch all products from Firestore, ordered by createdAt descending
     */
    fun getAllProducts(): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())

        val listenerRegistration = productsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error occurred"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Product::class.java)?.copy(id = doc.id)
                    }
                    trySend(Resource.Success(products))
                } else {
                    trySend(Resource.Error("No data available"))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    /**
     * Filter products by category
     */
    fun getProductsByCategory(category: String): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())

        val listenerRegistration = productsCollection
            .whereEqualTo("category", category)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error occurred"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Product::class.java)?.copy(id = doc.id)
                    }
                    trySend(Resource.Success(products))
                } else {
                    trySend(Resource.Error("No data available"))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    /**
     * Get single product by ID
     */
    suspend fun getProductById(productId: String): Resource<Product> {
        return try {
            val document = productsCollection.document(productId).get().await()
            if (document.exists()) {
                val product = document.toObject(Product::class.java)?.copy(id = document.id)
                if (product != null) {
                    Resource.Success(product)
                } else {
                    Resource.Error("Failed to parse product data")
                }
            } else {
                Resource.Error("Product not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    /**
     * Search products by name, description, or brand
     * Note: Firestore doesn't support full-text search natively.
     * For production, consider using Algolia, Elasticsearch, or Firestore's array-contains
     * with tokenized fields for better performance. Current implementation fetches all
     * products and filters client-side, which is acceptable for small datasets but
     * inefficient for large catalogs.
     */
    fun searchProducts(query: String): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())

        val listenerRegistration = productsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error occurred"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Product::class.java)?.copy(id = doc.id)
                    }.filter { product ->
                        product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true) ||
                        product.brand.contains(query, ignoreCase = true)
                    }
                    trySend(Resource.Success(products))
                } else {
                    trySend(Resource.Error("No data available"))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    /**
     * Get featured products for home screen
     */
    fun getFeaturedProducts(): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())

        val listenerRegistration = productsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(FEATURED_PRODUCTS_LIMIT)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error occurred"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Product::class.java)?.copy(id = doc.id)
                    }
                    trySend(Resource.Success(products))
                } else {
                    trySend(Resource.Error("No data available"))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
