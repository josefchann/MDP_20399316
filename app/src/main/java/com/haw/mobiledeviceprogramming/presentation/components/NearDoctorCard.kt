package com.haw.mobiledeviceprogramming.presentation.components

import DoctorViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.Orange
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.TextColorTitle
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import com.haw.mobiledeviceprogramming.presentation.utils.Doctor
import com.haw.mobiledeviceprogramming.presentation.utils.sampleRating
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NearDoctorCard(
    index: Int,
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel()
) {
    val doctors by viewModel.doctors.collectAsState() // Observe doctors
    val isLoading by viewModel.isLoading.collectAsState()

    // Get a distinct doctor based on the index
    val doctor = if (doctors.isNotEmpty() && index < doctors.size) {
        doctors[index]
    } else {
        null
    }

    when {
        isLoading && index == 0 -> {
            // Show loading indicator for the first card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = BluePrimary,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Fetching doctors, please wait...",
                    modifier = Modifier.padding(top = 8.dp),
                    color = PurpleGrey,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        doctor != null -> {
            // Render the card for the doctor
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
                            modifier = Modifier.size(48.dp),
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
                            title = sampleRating.random().rating,
                            color = Orange
                        )

                        BottomItem(
                            icon = R.drawable.ic_clock,
                            title = "Open at 17.00", // Hardcoded for now
                            color = BluePrimary
                        )
                    }
                }
            }
        }
    }
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

//@Preview
//@Composable
//private fun NearDoctorCardPreview() {
//    NearDoctorCard()
//}