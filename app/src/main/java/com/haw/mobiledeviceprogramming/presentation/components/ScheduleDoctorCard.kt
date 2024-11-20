package com.haw.mobiledeviceprogramming.presentation.components

import DoctorViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.TextColorTitle
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haw.mobiledeviceprogramming.presentation.utils.Doctor


@Composable
fun ScheduleDoctorCard(
    index: Int, // Add index parameter to get a distinct doctor
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel() // Use ViewModel
) {
    val doctor = remember {
        mutableStateOf<Doctor?>(null)
    }
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDoctors()
        doctor.value = viewModel.getDoctorForIndex(index) // Fetch doctor for this index
    }

    if (doctor.value != null) {
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
                Row {
                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = doctor.value!!.imageRes),
                        contentDescription = "Image of ${doctor.value!!.name}"
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f),
                    ) {
                        Text(
                            text = doctor.value!!.name,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TextColorTitle
                        )

                        Text(
                            text = doctor.value!!.specialty,
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

                ScheduleTimeContent(contentColor = PurpleGrey)

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF63B4FF).copy(alpha = 0.1f)),
                    onClick = { /* TODO: Navigate to detail screen */ }
                ) {
                    Text(
                        text = "Detail",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = BluePrimary
                    )
                }
            }
        }
    } else if (isLoading) {
        // Loading placeholder
        Text(
            text = "Loading...",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = PurpleGrey
        )
    }
}


//@Preview
//@Composable
//private fun ScheduleDoctorCardPreview() {
//    ScheduleDoctorCard()
//}