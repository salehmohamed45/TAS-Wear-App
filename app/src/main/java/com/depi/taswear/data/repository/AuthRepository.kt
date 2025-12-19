package com.depi.taswear.data.repository

import com.depi.taswear.data.model.User
import com.depi.taswear.data.model.UserRole
import com.depi.taswear.util.Constants
import com.depi.taswear.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    
    suspend fun login(email: String, password: String): Resource<User> {
        return try {
            // Sign in with Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return Resource.Error("User ID not found")
            
            // Fetch user data from Firestore
            val userDoc = firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .get()
                .await()
            
            val user = userDoc.toObject(User::class.java)
                ?: return Resource.Error("User data not found")
            
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed")
        }
    }
    
    suspend fun register(email: String, password: String, name: String): Resource<User> {
        return try {
            // Create user with Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return Resource.Error("User ID not found")
            
            // Create user object
            val user = User(
                id = userId,
                email = email,
                name = name,
                role = UserRole.CUSTOMER,
                createdAt = System.currentTimeMillis()
            )
            
            // Save user data to Firestore
            firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .set(user)
                .await()
            
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registration failed")
        }
    }
    
    fun logout() {
        auth.signOut()
    }
    
    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            name = firebaseUser.displayName ?: ""
        )
    }
}
