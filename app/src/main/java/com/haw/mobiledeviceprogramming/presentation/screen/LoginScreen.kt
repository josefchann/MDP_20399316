package com.haw.mobiledeviceprogramming

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.haw.mobiledeviceprogramming.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context) }
    val coroutineScope = rememberCoroutineScope()

    // Helper function to handle login responses
    fun handleAuthResponse(response: AuthResponse) {
        when (response) {
            is AuthResponse.Success -> {
                navController.navigate("main") // Navigate to Home
                Toast.makeText(context, "Login Success!", Toast.LENGTH_LONG).show()
            }
            is AuthResponse.Error -> {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "Unexpected response", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Animatable to control the vertical offset of the logo
    val logoOffsetY = remember { androidx.compose.animation.core.Animatable(50f) }

    // Launch the animation when the screen is first displayed
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            logoOffsetY.animateTo(
                targetValue = 0f, // Move logo to its final position
                animationSpec = tween(durationMillis = 2000) // 2-second animation
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Background color
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Firebase Logo with slide-up animation
            Image(
                painter = painterResource(id = R.drawable.img_logo), // Replace with Firebase logo
                contentDescription = "Logo",
                modifier = Modifier
                    .offset(y = logoOffsetY.value.dp) // Apply animation offset
                    .size(500.dp)
                    .align(Alignment.Center) // Align the logo at the top-center
                    .padding(bottom = 50.dp)
            )

            // Google Sign-In Button positioned at 3/4 of the screen height
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 150.dp), // Push the button upward to 3/4 position
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        isLoading = true
                        authenticationManager.signInWithGoogle(userViewModel)
                            .onEach { response ->
                                handleAuthResponse(response)
                                isLoading = false
                            }
                            .launchIn(coroutineScope)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp) // Adjust button width and height
                        .border(
                            width = 1.dp, // Outline thickness
                            color = MaterialTheme.colorScheme.onSurface, // Outline color
                            shape = RoundedCornerShape(12.dp) // Match the button's corner radius
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface, // White background for the button
                        contentColor = MaterialTheme.colorScheme.onSurface // Black text color
                    ),
                    shape = RoundedCornerShape(32.dp), // Button's corner radius
                    enabled = !isLoading
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_google), // Replace with Google logo
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sign in with Google",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }


                if (isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
