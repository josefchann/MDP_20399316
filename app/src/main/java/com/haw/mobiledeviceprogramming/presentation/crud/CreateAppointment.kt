package com.haw.mobiledeviceprogramming.presentation.crud

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.haw.mobiledeviceprogramming.presentation.utils.Doctor

// Appointment class
data class Appointment(
    val userUuid: String = "C2CcujZmsaZHK1qXmjQ4ARNFLuf1", // Default value to ensure no-argument constructor
    val appointmentDate: String = "", // Default value
    val doctor: Doctor = Doctor() // Default Doctor object for no-argument constructor
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this("", "", Doctor())
}

fun createAppointment(
    context: Context,
    appointment: Appointment,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()

    // Prepare Firestore data
    val appointmentData = hashMapOf(
        "userUuid" to appointment.userUuid,
        "appointmentDate" to appointment.appointmentDate,
        "doctor" to hashMapOf(
            "name" to appointment.doctor.name,
            "specialty" to appointment.doctor.specialty,
            "consultationFee" to appointment.doctor.consultationFee,
            "openingTime" to appointment.doctor.openingTime,
            "location" to appointment.doctor.location,
            "education" to appointment.doctor.education,
            "id" to appointment.doctor.id,
            "imageRes" to appointment.doctor.imageRes
        ),
    )

    // Save to Firestore under "appointments" collection
    firestore.collection("appointments")
        .add(appointmentData)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { onFailure(it) }
}
