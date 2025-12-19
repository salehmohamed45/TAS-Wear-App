package com.depi.taswear.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.taswear.data.model.Product
import com.depi.taswear.data.repository.ProductRepository
import com.depi.taswear.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _featuredProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val featuredProducts: StateFlow<Resource<List<Product>>> = _featuredProducts.asStateFlow()

    init {
        loadFeaturedProducts()
    }

    private fun loadFeaturedProducts() {
        viewModelScope.launch {
            productRepository.getFeaturedProducts().collect { resource ->
                _featuredProducts.value = resource
            }
        }
    }

    fun refreshProducts() {
        loadFeaturedProducts()
    }
}
