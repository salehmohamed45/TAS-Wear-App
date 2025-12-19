package com.depi.taswear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.depi.taswear.ui.screens.home.HomeScreen
import com.depi.taswear.ui.theme.TASWearTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TASWearTheme {
                HomeScreen(
                    onProductClick = { productId ->
                        // Product detail navigation will be implemented in Phase 3
                    }
                )
            }
        }
    }
}