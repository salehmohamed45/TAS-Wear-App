package com.depi.taswear.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.depi.taswear.ui.screens.auth.LoginScreen
import com.depi.taswear.ui.screens.auth.RegisterScreen
import com.depi.taswear.ui.screens.cart.CartScreen
import com.depi.taswear.ui.screens.checkout.CheckoutScreen
import com.depi.taswear.ui.screens.home.HomeScreen
import com.depi.taswear.ui.screens.product.ProductDetailScreen
import com.depi.taswear.ui.screens.product.ProductListScreen
import com.depi.taswear.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(
            route = Screen.ProductList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            ProductListScreen(navController = navController, category = category)
        }
        
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(navController = navController, productId = productId)
        }
        
        composable(Screen.Cart.route) {
            CartScreen(navController = navController)
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        
        composable(Screen.Checkout.route) {
            CheckoutScreen(navController = navController)
        }
    }
}
