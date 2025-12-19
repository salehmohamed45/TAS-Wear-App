package com.depi.taswear.ui.screens.product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ProductDetailScreen(navController: NavController, productId: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Product Detail Screen\nProduct ID: $productId",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
