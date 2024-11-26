package com.haw.mobiledeviceprogramming.presentation.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.haw.mobiledeviceprogramming.R
import kotlinx.coroutines.tasks.await

data class Doctor(
    val name: String = "",
    val specialty: String = "",
    val imageRes: Int = R.drawable.ic_doctor, // Default image resource
    val openingTime: String = "",
    val id: Int = 0, // Default value for id
    val education: String = "",
    val consultationFee: Int = 0, // Default value for consultationFee
    val location: String = ""
) {
    // No-argument constructor to support Firestore deserialization
    constructor() : this(
        name = "",
        specialty = "",
        imageRes = R.drawable.ic_doctor,
        openingTime = "",
        id = 0,
        education = "",
        consultationFee = 0,
        location = ""
    )
}

object DoctorRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun fetchDoctors(): List<Doctor> {
        return try {
            val snapshot = firestore.collection("doctors").get().await()
            snapshot.documents.mapNotNull { doc ->
                val name = doc.getString("name") ?: return@mapNotNull null
                val specialty = doc.getString("specialty") ?: return@mapNotNull null
                val imageName = doc.getString("imageRes") ?: return@mapNotNull null
                val openingTime = doc.getString("openingTime") ?: return@mapNotNull null
                val id = doc.getLong("id")?.toInt() ?: return@mapNotNull null
                val education = doc.getString("education") ?: return@mapNotNull null
                val consultationFee = doc.getLong("consultationFee")?.toInt() ?: return@mapNotNull null
                val location = doc.getString("location") ?: return@mapNotNull null

                // Map image names to drawable resources
                val imageRes = mapImageNameToResource(imageName)

                Doctor(name, specialty, imageRes, openingTime, id, education, consultationFee, location)
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching doctors: ${e.message}")
            emptyList()
        }
    }

    fun mapImageNameToResource(imageName: String): Int {
        val resource = when (imageName) {
            "img_ibnusina" -> R.drawable.img_ibnusina
            "img_ameliatan" -> R.drawable.img_ameliatan
            "img_michaelzhang" -> R.drawable.img_michaelzhang
            "img_sarahlee" -> R.drawable.img_sarahlee
            "img_johndoe" -> R.drawable.img_johndoe
            else -> R.drawable.ic_doctor
        }
        Log.d("ImageMapping", "Mapping imageName: $imageName to resource: $resource")
        return resource
    }
}
