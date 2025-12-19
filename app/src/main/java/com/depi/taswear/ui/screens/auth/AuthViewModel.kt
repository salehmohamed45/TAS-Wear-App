package com.depi.taswear.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.taswear.data.model.User
import com.depi.taswear.data.repository.AuthRepository
import com.depi.taswear.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _loginState = MutableStateFlow<Resource<User>?>(null)
    val loginState: StateFlow<Resource<User>?> = _loginState
    
    private val _registerState = MutableStateFlow<Resource<User>?>(null)
    val registerState: StateFlow<Resource<User>?> = _registerState
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            _loginState.value = authRepository.login(email, password)
        }
    }
    
    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading()
            _registerState.value = authRepository.register(email, password, name)
        }
    }
    
    fun logout() {
        authRepository.logout()
    }
    
    fun resetLoginState() {
        _loginState.value = null
    }
    
    fun resetRegisterState() {
        _registerState.value = null
    }
}
