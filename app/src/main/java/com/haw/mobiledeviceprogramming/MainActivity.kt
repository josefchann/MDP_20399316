package com.app.mobiledeviceprogramming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.haw.mobiledeviceprogramming.presentation.screen.MainScreen
import com.app.mobiledeviceprogramming.ui.theme.MobileDeviceProgrammingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileDeviceProgrammingTheme {
                MainScreen()
            }
        }
    }
}
