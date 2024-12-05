@file:OptIn(ExperimentalMaterial3Api::class)

package com.haw.mobiledeviceprogramming.presentation.components

import DoctorViewModel
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.Orange
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.TextColorTitle
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import com.haw.mobiledeviceprogramming.utils.Doctor
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.haw.mobiledeviceprogramming.viewmodel.Appointment
import com.haw.mobiledeviceprogramming.viewmodel.createAppointment
import com.haw.mobiledeviceprogramming.ui.theme.DarkBlue

@Composable
fun NearDoctorCard(
    doctor: Doctor
) {
    val showBottomSheet = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        tonalElevation = 0.5.dp,
        shadowElevation = 0.2.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Row {
                Image(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(0.5.dp, Color.Gray, CircleShape),
                    painter = painterResource(id = doctor.imageRes),
                    contentDescription = "Image of ${doctor.name}",
                )

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
                ) {
                    Text(
                        text = doctor.name,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TextColorTitle
                    )

                    Text(
                        text = doctor.specialty,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Light,
                        color = PurpleGrey
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .alpha(0.1f),
                color = Color.Gray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomItem(
                    icon = R.drawable.ic_review,
                    title = "Rated at ${doctor.rating}",
                    color = Orange
                )

                BottomItem(
                    icon = R.drawable.ic_clock,
                    title = "Opens at ${doctor.openingTime}",
                    color = BluePrimary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Trigger bottom sheet
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF63B4FF).copy(alpha = 0.1f)),
                    onClick = { showBottomSheet.value = true }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "For More Details",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = BluePrimary
                        )

                        Image(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Navigate to Details",
                            colorFilter = ColorFilter.tint(BluePrimary)
                        )
                    }
                }
            }
        }
    }

    // Bottom Sheet for Doctor Details
    if (showBottomSheet.value) {
        DoctorDetailsBottomSheet(
            doctorId = doctor.id,
            viewModel = viewModel(),
            onDismiss = { showBottomSheet.value = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailsBottomSheet(
    doctorId: Int,
    viewModel: DoctorViewModel,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        val doctor by remember { derivedStateOf { viewModel.getDoctorById(doctorId) } }
        val isLoading by viewModel.isLoading.collectAsState()
        var showDialog by remember { mutableStateOf(false) }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (doctor != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ProfileSection(doctor = doctor!!)
                Spacer(modifier = Modifier.height(24.dp))
                DoctorStats(doctor = doctor!!)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Request Appointment", color = Color.White)
                }
            }

            // Check if the dialog should be displayed
            if (showDialog) {
                AppointmentDialog(doctor = doctor!!, onDismiss = {

                    showDialog = false
                    println("Dialog dismissed")
                })
            }

        } else {
            Text(
                text = "Doctor details not found.",
                color = Color.Red,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AppointmentDialog(
    doctor: Doctor,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    var selectedDate by remember { mutableStateOf<String?>(null) }

    // Function to show DatePickerDialog
    fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the date as DD/MM/YYYY
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                selectedDate = formattedDate
            },
            year,
            month,
            day
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_date),
                    contentDescription = "Appointment Icon",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = "Appointment Details",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Doctor Info
                Text(
                    text = "Doctor: ${doctor.name}\n" +
                            "Specialty: ${doctor.specialty}\n" +
                            "Consultation Fee: RM${doctor.consultationFee}\n" +
                            "Available Timings: ${doctor.openingTime}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Date Picker Button
                Button(
                    onClick = { showDatePicker() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = if (selectedDate.isNullOrEmpty()) "Select Date" else "Selected Date: $selectedDate")
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Dismiss Button
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = MaterialTheme.colorScheme.primary)
                }

                // Confirm Button
                Button(
                    onClick = {
                        // Ensure the user has selected a date
                        if (!selectedDate.isNullOrEmpty()) {
                            val userUuid = currentUser?.uid
                            if (userUuid != null) {
                                val appointment = Appointment(
                                    userUuid = userUuid,
                                    doctor = doctor,
                                    appointmentDate = selectedDate!!
                                )
                                createAppointment(
                                    context = context,
                                    appointment = appointment,
                                    onSuccess = {
                                        Toast.makeText(context, "Appointment confirmed!", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    },
                                    onFailure = { exception ->
                                        Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            } else {
                                println("Error: User not signed in.")
                            }
                        } else {
                            println("Please select a date first")
                        }
                    },
                    enabled = !selectedDate.isNullOrEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0353A4),
                        contentColor = Color.White
                    )
                ) {
                    Text("Confirm")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp
    )
}


@Composable
private fun BottomItem(modifier: Modifier = Modifier, icon: Int, title: String, color: Color) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = icon),
            contentDescription = "Icon",
            colorFilter = ColorFilter.tint(color = color)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = title,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.W400,
            color = color,
            fontSize = 12.sp
        )
    }
}