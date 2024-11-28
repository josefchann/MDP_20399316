package com.haw.mobiledeviceprogramming.presentation.screen

import com.haw.mobiledeviceprogramming.presentation.components.DoctorDetailsScreen
import DoctorViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.presentation.components.NearDoctorCard
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.GraySecond
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.haw.mobiledeviceprogramming.presentation.crud.Appointment
import com.haw.mobiledeviceprogramming.presentation.utils.sampleDate
import com.haw.mobiledeviceprogramming.presentation.utils.sampleTime
import com.haw.mobiledeviceprogramming.presentation.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel() // Use the same UserViewModel instance here
) {
    Surface(
        modifier = modifier.padding(top = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            // Header Content
            HeaderContent(
                userViewModel = userViewModel // Pass UserViewModel directly here
            )

            Spacer(modifier = Modifier.height(32.dp))

//            // Schedule Content
            ScheduleContent()

            // Search Bar
            SearchableDoctorList(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun HomeScreenWithNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = "doctor_list"
    ) {
        // List Screen
        composable("doctor_list") {
            // Use the same UserViewModel instance here
            HomeScreen(navController = navController)
        }

        // Details Screen
        composable(
            route = "doctor_details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            DoctorDetailsScreen(id = id)
        }
    }
}

@Composable
private fun HeaderContent(
    modifier: Modifier = Modifier, userViewModel: UserViewModel
) {
    val user = userViewModel.user

    // Debugging Log to check if the user data is being updated
    Log.d("HeaderContent", "User Data: ${user.name}, ${user.profilePictureUrl}")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello,",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.W400,
                color = PurpleGrey
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Welcome Back",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        if (user.profilePictureUrl.isNotEmpty()) {
            Image(
                modifier = Modifier.size(56.dp),
                painter = painterResource(id = R.drawable.img_header_content),
//                painter = rememberAsyncImagePainter(user.profilePictureUrl),
                contentDescription = "User Profile Picture"
            )
        }
    }
}

@Composable
private fun ScheduleContent(
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel(),
) {
    val doctors by viewModel.doctors.collectAsState()
    val appointments by viewModel.appointments.collectAsState()

    // Trigger fetching of doctors and appointments
    LaunchedEffect(Unit) {
        viewModel.fetchDoctors()
        viewModel.fetchUserAppointmentsAndDoctors()
    }

    // Get a random appointment or show loading state
    val appointment = if (appointments.isNotEmpty()) appointments.random() else null

    // Conditional rendering based on appointment status
    if (appointment != null) {
        // Display the upcoming appointment details
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = BluePrimary,
            tonalElevation = 1.dp,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = "Your Upcoming Appointment",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold, color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp), // Adds spacing below the header
                    textAlign = TextAlign.Start
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .height(1.dp)
                        .alpha(0.2f), color = Color.White
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = appointment.doctor.imageRes),
                        contentDescription = "Image Doctor"
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = appointment.doctor.name,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = appointment.doctor.specialty,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Light,
                            color = GraySecond
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(1.dp)
                        .alpha(0.2f), color = Color.White
                )

                // Schedule Time Content
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Content(
                        icon = R.drawable.ic_date,
                        title = appointment.appointmentDate,
                        contentColor = Color.White
                    )

                    Content(
                        icon = R.drawable.ic_clock,
                        title = appointment.doctor.openingTime,
                        contentColor = Color.White
                    )
                }
            }
        }
    } else {
        // Show a "No upcoming appointments" placeholder as a card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF5F5F5)
        ) {
            Box(
                modifier = Modifier.padding(24.dp), // Add padding inside the card
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No upcoming appointments",
                    color = Color.Gray,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Composable
fun SearchableDoctorList(
    navController: NavController, viewModel: DoctorViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val doctors by viewModel.doctors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "Search for Doctor",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W400,
                    color = PurpleGrey
                )
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon"
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        val filteredDoctors = if (searchQuery.isBlank()) {
            doctors
        } else {
            doctors.filter {
                it.name.contains(searchQuery, ignoreCase = true) || it.specialty.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
        }


        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = BluePrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fetching doctors, please wait...",
                    color = PurpleGrey,
                    fontFamily = poppinsFontFamily
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filteredDoctors) { doctor ->
                    NearDoctorCard(doctor = doctor)
                }
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier, icon: Int, title: String, contentColor: Color
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = icon),
            colorFilter = ColorFilter.tint(color = contentColor),
            contentDescription = "Icon Date"
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.W400,
            color = contentColor,
            fontSize = 12.sp
        )
    }
}
