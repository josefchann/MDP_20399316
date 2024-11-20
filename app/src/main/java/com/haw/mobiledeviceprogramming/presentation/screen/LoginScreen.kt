package com.haw.mobiledeviceprogramming

import android.content.Intent
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context) }
    val coroutineScope = rememberCoroutineScope()

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
            text = "Please fill in the form to continue",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { newValue -> email = newValue },
            placeholder = { Text(text = "Email") },
            leadingIcon = { Icon(imageVector = Icons.Rounded.Email, contentDescription = null) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { newValue -> password = newValue },
            placeholder = { Text(text = "Password") },
            leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                authenticationManager.loginWithEmail(email, password)
                    .onEach { response ->
                        when (response) {
                            is AuthResponse.Success -> {
                                // Navigate to Home
                                navController.navigate(BottomNavItemScreen.Home.route)
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
                    .launchIn(coroutineScope)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sign-in",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "or continue with")
        }

        OutlinedButton(
            onClick = {
                authenticationManager.signInWithGoogle()
                    .onEach { response ->
                        when (response) {
                            is AuthResponse.Success -> {
                                // Navigate to Home
                                navController.navigate(BottomNavItemScreen.Home.route) {
                                    popUpTo("login") { inclusive = true }
                                }
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
                    .launchIn(coroutineScope)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
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
