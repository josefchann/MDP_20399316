package com.haw.mobiledeviceprogramming.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.haw.mobiledeviceprogramming.presentation.utils.MedicalHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData to observe profile data
    private val _profiles = MutableLiveData<List<Map<String, Any>>>()
    val profiles: LiveData<List<Map<String, Any>>> = _profiles

    private val db = FirebaseFirestore.getInstance()

    // Function to fetch profiles from Firestore
    fun fetchProfiles(userUuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("medicalHistory")
                    .whereEqualTo("userUuid", userUuid) // Filter by user UUID
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
                            profileList.add(mapOf("condition" to condition, "date" to date))
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
        val profileData = hashMapOf(
            "medicalCondition" to medicalHistory.medicalCondition,
            "date" to medicalHistory.date, // Store the date as a String
            "userUuid" to userUuid // Include the user UUID
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("medicalHistory")
                    .add(profileData)
                    .addOnSuccessListener {
                        fetchProfiles(userUuid) // Refresh profiles after successful addition, filtering by userUuid
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error adding profile info: ", e)
                    }
            } catch (e: Exception) {
                Log.e("ViewModelError", "Unexpected error: ", e)
            }
        }
    }





}
