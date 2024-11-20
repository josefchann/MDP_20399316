package com.haw.mobiledeviceprogramming.presentation.utils

import com.haw.mobiledeviceprogramming.R

data class MedicalHistory(
    val desc: String,
)

val sampleMedicalHistory = listOf(
    MedicalHistory("Hypertension"),
    MedicalHistory("Diabetes"),
    MedicalHistory("Asthma"),
    MedicalHistory("Arthritis"),
    MedicalHistory("Chronic Kidney Disease")
)
