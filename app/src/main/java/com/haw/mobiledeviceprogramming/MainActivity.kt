package com.haw.mobiledeviceprogramming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haw.mobiledeviceprogramming.presentation.screen.MainScreen
import com.haw.mobiledeviceprogramming.presentation.viewmodel.UserViewModel
import com.haw.mobiledeviceprogramming.ui.theme.MobileDeviceProgrammingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileDeviceProgrammingTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    // Create UserViewModel
                    val userViewModel: UserViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            // Pass the viewModel to LoginScreen
                            LoginScreen(navController = navController, userViewModel = userViewModel)
                        }
                        composable("main") {
                            MainScreen()
                        }
                    }
                }
            }
        }
    }
}
