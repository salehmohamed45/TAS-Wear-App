package com.depi.taswear.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.taswear.data.model.Product
import com.depi.taswear.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling product operations
 */
class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    private val _productsState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productsState: StateFlow<ProductsState> = _productsState.asStateFlow()
    
    private val _featuredProduct = MutableStateFlow<Product?>(null)
    val featuredProduct: StateFlow<Product?> = _featuredProduct.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    init {
        loadProducts()
        loadFeaturedProduct()
    }
    
    /**
     * Load all products
     */
    fun loadProducts() {
        viewModelScope.launch {
            _productsState.value = ProductsState.Loading
            val result = productRepository.getAllProducts()
            
            _productsState.value = if (result.isSuccess) {
                ProductsState.Success(result.getOrNull() ?: emptyList())
            } else {
                ProductsState.Error(result.exceptionOrNull()?.message ?: "Failed to load products")
            }
        }
    }
    
    /**
     * Load products by category
     */
    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            _productsState.value = ProductsState.Loading
            _selectedCategory.value = category
            val result = productRepository.getProductsByCategory(category)
            
            _productsState.value = if (result.isSuccess) {
                ProductsState.Success(result.getOrNull() ?: emptyList())
            } else {
                ProductsState.Error(result.exceptionOrNull()?.message ?: "Failed to load products")
            }
        }
    }
    
    /**
     * Search products
     */
    fun searchProducts(query: String) {
        if (query.isBlank()) {
            loadProducts()
            return
        }
        
        viewModelScope.launch {
            _productsState.value = ProductsState.Loading
            val result = productRepository.searchProducts(query)
            
            _productsState.value = if (result.isSuccess) {
                ProductsState.Success(result.getOrNull() ?: emptyList())
            } else {
                ProductsState.Error(result.exceptionOrNull()?.message ?: "Search failed")
            }
        }
    }
    
    /**
     * Load featured product (Featured Look)
     */
    fun loadFeaturedProduct() {
        viewModelScope.launch {
            val result = productRepository.getFeaturedProduct()
            if (result.isSuccess) {
                _featuredProduct.value = result.getOrNull()
            }
        }
    }
    
    /**
     * Clear category filter
     */
    fun clearCategoryFilter() {
        _selectedCategory.value = null
        loadProducts()
    }
    
    /**
     * Add product (Admin only)
     */
    fun addProduct(product: Product) {
        viewModelScope.launch {
            val result = productRepository.addProduct(product)
            if (result.isSuccess) {
                loadProducts()
            }
        }
    }
    
    /**
     * Update product (Admin only)
     */
    fun updateProduct(productId: String, product: Product) {
        viewModelScope.launch {
            val result = productRepository.updateProduct(productId, product)
            if (result.isSuccess) {
                loadProducts()
            }
        }
    }
}

/**
 * Sealed class representing product loading states
 */
sealed class ProductsState {
    object Loading : ProductsState()
    data class Success(val products: List<Product>) : ProductsState()
    data class Error(val message: String) : ProductsState()
}
