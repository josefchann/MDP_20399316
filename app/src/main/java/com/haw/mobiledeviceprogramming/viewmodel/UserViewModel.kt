package com.haw.mobiledeviceprogramming.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class User(
    val name: String = "User",
    val profilePictureUrl: String = ""
)

class UserViewModel : ViewModel() {
    var user by mutableStateOf(User())
        private set

    var isSignedIn by mutableStateOf(false)
        private set

    fun setUser(name: String, profilePictureUrl: String) {
        Log.d("UserViewModel", "Setting user - Name: $name, Profile Picture URL: $profilePictureUrl")
        user = User(name, profilePictureUrl)
        isSignedIn = true
        Log.d("UserViewModel", "isSignedIn updated to: $isSignedIn")
    }

    fun signOut() {
        Firebase.auth.signOut()

        user = User()
        isSignedIn = false
    }

}

