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
import com.haw.mobiledeviceprogramming.viewmodel.UserViewModel
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
                navController.navigate("main")
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
                targetValue = 0f,
                animationSpec = tween(durationMillis = 2000)
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Firebase Logo with slide-up animation
            Image(
                painter = painterResource(id = R.drawable.img_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .offset(y = logoOffsetY.value.dp)
                    .size(500.dp)
                    .align(Alignment.Center)
                    .padding(bottom = 50.dp)
            )

            // Google Sign-In Button positioned at 3/4 of the screen height
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 150.dp),
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
                        .height(56.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(32.dp),
                    enabled = !isLoading
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_google),
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
