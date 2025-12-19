package com.depi.taswear.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.taswear.data.model.User
import com.depi.taswear.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling authentication state and operations
 */
class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isGuestMode = MutableStateFlow(true)
    val isGuestMode: StateFlow<Boolean> = _isGuestMode.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    /**
     * Check if user is already logged in
     */
    private fun checkAuthStatus() {
        val user = authRepository.getCurrentUser()
        if (user != null) {
            _currentUser.value = user
            _isGuestMode.value = false
            _authState.value = AuthState.Authenticated(user)
        } else {
            _isGuestMode.value = true
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    /**
     * Sign in with email and password
     */
    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signIn(email, password)
            
            _authState.value = if (result.isSuccess) {
                val user = result.getOrNull()!!
                _currentUser.value = user
                _isGuestMode.value = false
                AuthState.Authenticated(user)
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Sign in failed")
            }
        }
    }
    
    /**
     * Create new account
     */
    fun signUp(email: String, password: String, role: String = "customer") {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        
        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signUp(email, password, role)
            
            _authState.value = if (result.isSuccess) {
                val user = result.getOrNull()!!
                _currentUser.value = user
                _isGuestMode.value = false
                AuthState.Authenticated(user)
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Sign up failed")
            }
        }
    }
    
    /**
     * Sign out current user
     */
    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _isGuestMode.value = true
        _authState.value = AuthState.Unauthenticated
    }
    
    /**
     * Continue as guest
     */
    fun continueAsGuest() {
        _isGuestMode.value = true
        _authState.value = AuthState.Guest
    }
    
    /**
     * Reset auth state
     */
    fun resetAuthState() {
        _authState.value = if (_currentUser.value != null) {
            AuthState.Authenticated(_currentUser.value!!)
        } else {
            AuthState.Unauthenticated
        }
    }
}

/**
 * Sealed class representing authentication states
 */
sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    object Guest : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}
