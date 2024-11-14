package com.haw.mobiledeviceprogramming.presentation.utils

import com.app.mobiledeviceprogramming.R

data class Doctor(
    val name: String,
    val specialty: String,
    val imageRes: Int
)

val sampleDoctors = listOf(
    Doctor("Dr. Ibnu Sina", "General Doctor", R.drawable.img_ibnusina),
    Doctor("Dr. Amelia Tan", "Cardiologist", R.drawable.img_ameliatan),
    Doctor("Dr. Michael Zhang", "Pediatrician", R.drawable.img_michaelzhang),
    Doctor("Dr. Sarah Lee", "Dermatologist", R.drawable.img_sarahlee),
    Doctor("Dr. John Doe", "Neurologist", R.drawable.img_johndoe)
)

fun getRandomDoctor(): Doctor {
    return sampleDoctors.random()
}