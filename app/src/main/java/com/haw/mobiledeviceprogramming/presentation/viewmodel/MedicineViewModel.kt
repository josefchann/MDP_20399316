package com.haw.mobiledeviceprogramming.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.haw.mobiledeviceprogramming.presentation.utils.Medicine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MedicineViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // StateFlow to hold the list of medicines
    private val _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    val medicines: StateFlow<List<Medicine>> = _medicines

    private val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Incoming format
    private val dbDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Format for storage and comparison

    // Function to calculate date difference
    private fun calculateDateDifference(restockDateString: String?): Long? {
        if (restockDateString.isNullOrBlank()) {
            Log.e("MedicineViewModel", "Invalid or empty restockDate")
            return null
        }

        return try {
            val restockDate = dbDateFormat.parse(restockDateString) // Parse using storage format
            val currentDate = Date()

            val diffInMillis = restockDate.time - currentDate.time
            val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
            Log.d("MedicineViewModel", "Date difference for $restockDateString: $diffInDays days")
            diffInDays
        } catch (e: ParseException) {
            Log.e("MedicineViewModel", "Error parsing date: $restockDateString", e)
            null
        }
    }

    // Function to fetch medicines for a specific user
    fun fetchMedicinesForUser(userUuid: String) {
        db.collection("medicines")
            .whereEqualTo("userUuid", userUuid)
            .get()
            .addOnSuccessListener { snapshot ->
                val medicineList = snapshot.documents.mapNotNull { doc ->
                    val medicine = doc.toObject(Medicine::class.java)
                    if (medicine != null) {
                        val diffInDays = calculateDateDifference(medicine.restockDate)

                        // Include only medicines where the restock date is today or in the future
                        if (diffInDays != null && diffInDays >= 0) {
                            medicine
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }

                _medicines.value = medicineList
            }
            .addOnFailureListener { exception ->
                Log.e("MedicineViewModel", "Error fetching medicines: ${exception.message}")
            }
    }

    // Function to add a medicine to the database
    fun addMedicineToDatabase(
        medicineName: String,
        medicineUsage: String,
        restockDate: String,
        userUuid: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            // Convert the incoming date from dd/MM/yyyy to yyyy-MM-dd
            val parsedDate = inputDateFormat.parse(restockDate) // Parse the input format
            val formattedDate = dbDateFormat.format(parsedDate) // Format to yyyy-MM-dd

            val medicine = hashMapOf(
                "medicineName" to medicineName,
                "medicineUsage" to medicineUsage,
                "restockDate" to formattedDate,
                "userUuid" to userUuid
            )

            db.collection("medicines")
                .add(medicine)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { exception -> onError(exception) }
        } catch (e: ParseException) {
            Log.e("MedicineViewModel", "Invalid date format: $restockDate", e)
            onError(e)
        }
    }
}

