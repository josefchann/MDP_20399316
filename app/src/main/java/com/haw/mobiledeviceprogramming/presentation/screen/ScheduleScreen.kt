package com.haw.mobiledeviceprogramming.presentation.screen

import DoctorViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haw.mobiledeviceprogramming.presentation.components.ScheduleDoctorCard
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel(),
) {
    val appointments by viewModel.appointments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserAppointmentsAndDoctors()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Appointments",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            color = BluePrimary,
            fontSize = 24.sp,
            textAlign = TextAlign.Start
        )

        if (isLoading) {
            Text(
                text = "Loading appointments...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            // Check if the appointments list is empty and display a message if it is
            if (appointments.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Scheduled Appointments.",
                        fontFamily = poppinsFontFamily,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Display the list of appointments if not empty
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(appointments) { appointment ->
                        ScheduleDoctorCard(
                            doctor = appointment.doctor,
                            appointment = appointment,
                            onDeleteClick = { appointmentId ->
                                viewModel.deleteAppointment(
                                    appointmentId = appointmentId,
                                    onSuccess = {
//                                        Toast.makeText(context, "Appointment deleted successfully.", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = {
//                                        Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        )
                    }

                }
            }
        }
    }
}
