package com.haw.mobiledeviceprogramming.presentation.screen

import DoctorViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import com.haw.mobiledeviceprogramming.presentation.utils.sampleRating


@Composable
fun DoctorDetailsScreen(id: Int?, viewModel: DoctorViewModel = viewModel()) {
    val doctors by viewModel.doctors.collectAsState()
    val doctor = doctors.find { it.id == id }

    if (doctor != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = doctor.name,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = doctor.specialty,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                color = PurpleGrey
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Rating: ⭐ ${sampleRating.random()}",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Opens at: ${doctor.openingTime}",
                fontFamily = poppinsFontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        Text(
            text = "Doctor details not found.",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
