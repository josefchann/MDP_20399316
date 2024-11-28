package com.haw.mobiledeviceprogramming


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.haw.mobiledeviceprogramming.navigation.screen.BottomNavItemScreen
import com.haw.mobiledeviceprogramming.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context) }
    val coroutineScope = rememberCoroutineScope()

    // Helper function to handle login responses
    fun handleAuthResponse(response: AuthResponse) {
        when (response) {
            is AuthResponse.Success -> {

                // Navigate to Home
                navController.navigate("main")
                Toast.makeText(context, "Login Success!", Toast.LENGTH_LONG).show()
            }
            is AuthResponse.Error -> {
                // Show error message
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
            }
            else -> {
                // Handle unexpected cases
                Toast.makeText(context, "Unexpected response", Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign-in",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Please sign-in to continue",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(15.dp))

//        OutlinedTextField(
//            value = email,
//            onValueChange = { newValue -> email = newValue },
//            placeholder = { Text(text = "Email") },
//            leadingIcon = { Icon(imageVector = Icons.Rounded.Email, contentDescription = null) },
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { newValue -> password = newValue },
//            placeholder = { Text(text = "Password") },
//            leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = null) },
//            visualTransformation = PasswordVisualTransformation(),
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        Button(
//            onClick = {
//                isLoading = true
//                authenticationManager.loginWithEmail(email, password)
//                    .onEach { response ->
//                        handleAuthResponse(response)
//                        isLoading = false
//                    }
//                    .launchIn(coroutineScope)
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !isLoading
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
//            } else {
//                Text(
//                    text = "Sign-in",
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "or continue with")
//        }

        OutlinedButton(
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
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            enabled = !isLoading
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_google),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Sign-in with Google",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

