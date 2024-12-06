package com.haw.mobiledeviceprogramming.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.TextColorTitle
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.viewmodel.Appointment
import com.haw.mobiledeviceprogramming.utils.Doctor


@Composable
fun ScheduleDoctorCard(
    doctor: Doctor,
    modifier: Modifier = Modifier,
    appointment: Appointment,
    onDetailClick: (Doctor) -> Unit = {},
    onDeleteClick: (Int) -> Unit = {}
) {
    var showCancelDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        tonalElevation = 0.5.dp,
        shadowElevation = 0.2.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(0.5.dp, Color.Gray, CircleShape),
                        painter = painterResource(id = doctor.imageRes),
                        contentDescription = "Image of ${doctor.name}"
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
                ScheduleContent(
                    icon = R.drawable.ic_date,
                    title = appointment.appointmentDate,
                    contentColor = PurpleGrey
                )

                ScheduleContent(
                    icon = R.drawable.ic_clock,
                    title = doctor.openingTime,
                    contentColor = PurpleGrey
                )
            }

            // Delete Button
            Button(
                onClick = { showCancelDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFEFEF),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Cancel Appointment",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFontFamily,
                    fontSize = 12.sp
                )
            }
        }
    }

    // Cancel Appointment Confirmation Dialog
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = {
                Text(
                    text = "Cancel Appointment",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to cancel this appointment?",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelDialog = false
                        onDeleteClick(appointment.doctor.id)
                    }
                ) {
                    Text("Yes, Cancel")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCancelDialog = false }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Composable
private fun ScheduleContent(
    icon: Int,
    title: String,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            color = contentColor,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
