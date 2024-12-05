package com.haw.mobiledeviceprogramming.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.haw.mobiledeviceprogramming.utils.MedicalHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData to observe profile data
    private val _profiles = MutableLiveData<List<Map<String, Any>>>()
    val profiles: LiveData<List<Map<String, Any>>> = _profiles

    private val db = FirebaseFirestore.getInstance()

    // Function to generate a sequential ID based on timestamp
    private fun generateSequentialId(): String {
        val timestamp = System.currentTimeMillis()
        val randomPart = (1000..9999).random()
        return "$timestamp-$randomPart"
    }

    fun fetchProfiles(userUuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("medicalHistory")
                    .whereEqualTo("userUuid", userUuid)
                    .get()
                    .addOnSuccessListener { result ->
                        val profileList = mutableListOf<Map<String, Any>>()
                        for (document in result) {
                            val condition = document.getString("medicalCondition") ?: ""
                            val date = when (val rawDate = document.get("date")) {
                                is String -> rawDate
                                is Long -> {
                                    val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                    formatter.format(java.util.Date(rawDate))
                                }
                                else -> ""
                            }
                            val id = document.id // Fetch Firestore document ID
                            profileList.add(
                                mapOf(
                                    "id" to id,          // Include the document ID
                                    "condition" to condition,
                                    "date" to date
                                )
                            )
                        }
                        _profiles.postValue(profileList)
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error fetching profiles: ", e)
                    }
            } catch (e: Exception) {
                Log.e("ViewModelError", "Unexpected error: ", e)
            }
        }
    }


    // Function to add new profile info to Firestore
    fun addProfileInfo(medicalHistory: MedicalHistory, userUuid: String) {
        val sequentialId = generateSequentialId()
        val profileData = hashMapOf(
            "id" to sequentialId,
            "medicalCondition" to medicalHistory.medicalCondition,
            "date" to medicalHistory.date,
            "userUuid" to userUuid
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("medicalHistory")
                    .document(sequentialId)
                    .set(profileData)
                    .addOnSuccessListener {
                        fetchProfiles(userUuid)
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error adding profile info: ", e)
                    }
            } catch (e: Exception) {
                Log.e("ViewModelError", "Unexpected error: ", e)
            }
        }
    }

    fun deleteProfileInfo(documentId: String, userUuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("medicalHistory")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        // Log success
                        Log.d("FirestoreSuccess", "Profile info deleted successfully.")

                        // Show a Toast message
                        viewModelScope.launch(Dispatchers.Main) {
                            Toast.makeText(
                                getApplication(),
                                "Item deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        // Refresh profiles after deletion
                        fetchProfiles(userUuid)
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error deleting profile info: ", e)

                        // Optionally show a Toast for failure
                        viewModelScope.launch(Dispatchers.Main) {
                            Toast.makeText(
                                getApplication(),
                                "Failed to delete item. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } catch (e: Exception) {
                Log.e("ViewModelError", "Unexpected error: ", e)

                // Optionally show a Toast for unexpected errors
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication(),
                        "An unexpected error occurred. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}

