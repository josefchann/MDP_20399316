package com.haw.mobiledeviceprogramming.presentation.components

import DoctorViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haw.mobiledeviceprogramming.presentation.utils.Doctor
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary

@Composable
fun DoctorDetailsScreen(id: Int?, viewModel: DoctorViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchDoctors()
    }

    val doctors by viewModel.doctors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (doctors.isNotEmpty() && id != null) {
        val doctor = viewModel.getDoctorById(id)
        if (doctor != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Profile Section
                ProfileSection(doctor = doctor)

                // Doctor Stats
                Spacer(modifier = Modifier.height(24.dp))
                DoctorStats(doctor = doctor)

                // Appointment Button
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { /* Navigate to appointment screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Request Appointment", color = Color.White)
                }

            }
        } else {
            Text(
                text = "Doctor with ID $id not found.",
                color = Color.Red,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    } else {
        Text(
            text = "No doctors available.",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileSection(doctor: Doctor) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                color = BluePrimary, // Use your custom BluePrimary color
                shape = RoundedCornerShape(12.dp) // Rounded corners
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center // Center all content inside the Box
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Center the content horizontally
        ) {
            Image(
                painter = painterResource(id = doctor.imageRes),
                contentDescription = "Image of ${doctor.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = doctor.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = doctor.specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DoctorStats(doctor: Doctor) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatsRow(label = "Education", value = doctor.education)
        StatsRow(label = "Available Timings", value = doctor.openingTime)
        StatsRow(label = "Consultation Fee", value = "RM${doctor.consultationFee} / Consultation")
    }
}

@Composable
fun StatsRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
