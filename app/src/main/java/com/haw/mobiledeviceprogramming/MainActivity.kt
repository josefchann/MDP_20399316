package com.haw.mobiledeviceprogramming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haw.mobiledeviceprogramming.presentation.screen.HomeScreen
import com.haw.mobiledeviceprogramming.presentation.screen.MainScreen
import com.haw.mobiledeviceprogramming.ui.theme.MobileDeviceProgrammingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileDeviceProgrammingTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(navController = navController)
                            //MainScreen()
                        }
                        composable("home") {
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}
