package com.depi.taswear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.depi.taswear.ui.navigation.NavGraph
import com.depi.taswear.ui.navigation.Screen
import com.depi.taswear.ui.theme.TASWearTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TASWearTheme {
                val navController = rememberNavController()
                
                // Determine start destination based on auth state
                val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
                    Screen.Home.route
                } else {
                    Screen.Login.route
                }
                
                NavGraph(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}