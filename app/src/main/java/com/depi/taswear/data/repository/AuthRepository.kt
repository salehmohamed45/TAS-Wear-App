package com.depi.taswear.data.repository

import com.depi.taswear.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repository for handling authentication operations using Firebase Auth
 */
class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    
    /**
     * Get current logged in user
     */
    fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser
        return firebaseUser?.let {
            User(
                uid = it.uid,
                email = it.email ?: ""
            )
        }
    }
    
    /**
     * Check if user is logged in
     */
    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
    
    /**
     * Sign in with email and password
     */
    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                // Fetch user data from Firestore
                val userDoc = firestore.collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                
                val user = if (userDoc.exists()) {
                    User(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email ?: "",
                        role = userDoc.getString("role") ?: "customer"
                    )
                } else {
                    User(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email ?: "",
                        role = "customer"
                    )
                }
                Result.success(user)
            } else {
                Result.failure(Exception("Authentication failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Create new account with email and password
     */
    suspend fun signUp(email: String, password: String, role: String = "customer"): Result<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = email,
                    role = role
                )
                
                // Store user data in Firestore
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(user)
                    .await()
                
                Result.success(user)
            } else {
                Result.failure(Exception("Account creation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Sign out current user
     */
    fun signOut() {
        firebaseAuth.signOut()
    }
    
    /**
     * Get user role from Firestore
     */
    suspend fun getUserRole(uid: String): Result<String> {
        return try {
            val userDoc = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            
            val role = userDoc.getString("role") ?: "customer"
            Result.success(role)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get all registered users (Admin only)
     */
    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users")
                .get()
                .await()
            
            val users = snapshot.documents.mapNotNull { doc ->
                doc.toObject(User::class.java)
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
