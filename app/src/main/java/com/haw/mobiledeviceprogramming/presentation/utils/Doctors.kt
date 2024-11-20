package com.haw.mobiledeviceprogramming.presentation.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.haw.mobiledeviceprogramming.R
import kotlinx.coroutines.tasks.await

data class Doctor(
    val name: String = "",
    val specialty: String = "",
    val imageRes: Int = R.drawable.ic_doctor // Placeholder image
)

object DoctorRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun fetchDoctors(): List<Doctor> {
        return try {
            val snapshot = firestore.collection("doctors").get().await()
            snapshot.documents.mapNotNull { doc ->
                val name = doc.getString("name") ?: return@mapNotNull null
                val specialty = doc.getString("specialty") ?: return@mapNotNull null
                val imageName = doc.getString("imageRes") ?: return@mapNotNull null

                // Map image names to drawable resources
                val imageRes = mapImageNameToResource(imageName)

                Doctor(name, specialty, imageRes)
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching doctors: ${e.message}")
            emptyList()
        }
    }

    private fun mapImageNameToResource(imageName: String): Int {
        return when (imageName) {
            "img_ibnusina" -> R.drawable.img_ibnusina
            "img_ameliatan" -> R.drawable.img_ameliatan
            "img_michaelzhang" -> R.drawable.img_michaelzhang
            "img_sarahlee" -> R.drawable.img_sarahlee
            "img_johndoe" -> R.drawable.img_johndoe
            else -> R.drawable.ic_doctor
        }
    }
}
