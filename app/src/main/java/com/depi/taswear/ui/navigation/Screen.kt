package com.depi.taswear.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object ProductList : Screen("product_list/{category}") {
        fun createRoute(category: String) = "product_list/$category"
    }
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Checkout : Screen("checkout")
}
