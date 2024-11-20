package com.haw.mobiledeviceprogramming.presentation.screen

import DoctorViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haw.mobiledeviceprogramming.presentation.components.ScheduleDoctorCard
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel()
) {
    val doctors by viewModel.doctors.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDoctors()
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (doctors.isEmpty()) {
                item {
                    Text(
                        text = "Loading doctors...",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                items(5) { index -> // Adjust the count as needed
                    ScheduleDoctorCard(index = index, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun CategorySchedule(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        text = "Appointments",
        fontFamily = poppinsFontFamily,
        color = BluePrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.Start
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScheduleScreenPreview() {
    ScheduleScreen()
}