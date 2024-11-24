package com.haw.mobiledeviceprogramming.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

data class User(
    val name: String = "User",
    val profilePictureUrl: String = ""
)

class UserViewModel : ViewModel() {
    var user by mutableStateOf(User())
        private set

    fun setUser(name: String, profilePictureUrl: String) {
        Log.d("UserViewModel", "Setting user - Name: $name, Profile Picture URL: $profilePictureUrl")
        user = User(name, profilePictureUrl)
        Log.d("UserViewModel", "Updated user: $user")
    }

}