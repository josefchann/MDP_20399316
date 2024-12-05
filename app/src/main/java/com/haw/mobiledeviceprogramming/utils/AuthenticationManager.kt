package com.haw.mobiledeviceprogramming

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.haw.mobiledeviceprogramming.viewmodel.UserViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID


class AuthenticationManager(val context: Context) {
    private val auth = Firebase.auth

    private val web_client_id = context.getString(R.string.web_client_id)

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun signInWithGoogle(userViewModel: UserViewModel): Flow<AuthResponse> = callbackFlow {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(web_client_id)
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context = context, request = request)
            val credential = result.credential

            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider.getCredential(
                            googleIdTokenCredential.idToken, null
                        )

                        auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("GoogleSignIn", "Authentication task successful.")

                                // Retrieve the user information from Firebase
                                val firebaseUser = auth.currentUser
                                if (firebaseUser != null) {
                                    Log.d("GoogleSignIn", "User Display Name: ${firebaseUser.displayName}")
                                    Log.d("GoogleSignIn", "User Profile Picture URL: ${firebaseUser.photoUrl}")
                                } else {
                                    Log.e("GoogleSignIn", "auth.currentUser is null, cannot retrieve user info.")
                                }

                                val userName = firebaseUser?.displayName ?: "User"
                                val userProfilePictureUrl = firebaseUser?.photoUrl?.toString() ?: ""

                                Log.d("GoogleSignIn", "Final User Name: $userName")
                                Log.d("GoogleSignIn", "Final User Profile Picture URL: $userProfilePictureUrl")

                                // Update the ViewModel with the user's name and profile picture URL
                                userViewModel.setUser(userName, userProfilePictureUrl)

                                trySend(AuthResponse.Success)
                            } else {
                                val errorMessage = task.exception?.message ?: "Unknown error"
                                Log.e("GoogleSignIn", "Authentication task failed. Error: $errorMessage")
                                trySend(AuthResponse.Error(errorMessage))
                            }

                        }
                    } catch (e: GoogleIdTokenParsingException) {
                        trySend(AuthResponse.Error(e.message ?: "Unknown error"))
                    }
                }
            }

        } catch (e: Exception) {
            trySend(AuthResponse.Error(message = e.message ?: "Unknown error"))
        }

        awaitClose()
    }


}

sealed interface AuthResponse {
    object Success : AuthResponse
    data class Error(val message: String) : AuthResponse
}
