package com.haw.mobiledeviceprogramming.presentation.screen

import com.haw.mobiledeviceprogramming.presentation.components.DoctorDetailsScreen
import DoctorViewModel
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
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
import com.haw.mobiledeviceprogramming.LoginScreen
import com.haw.mobiledeviceprogramming.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    Surface(
        modifier = modifier.padding(top = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            // Header section with user greeting and sign-out functionality
            HeaderContent(
                userViewModel = userViewModel,
                onSignOut = {
                    userViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Section for displaying user appointments
            ScheduleContent()

            // Section for listing doctors
            DoctorList(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun HomeScreenNavigation() {
    val navController = rememberNavController()
    val userViewModel = UserViewModel()

    NavHost(
        navController = navController, startDestination = "doctor_list"
    ) {
        // Screen for listing doctors
        composable("doctor_list") {
            HomeScreen(navController = navController)
        }

        // Screen for user login
        composable("login") {
            LoginScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        // Screen for displaying doctor details
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
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    onSignOut: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hey there!",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = PurpleGrey
            )
        }

        // Dropdown menu for user options (e.g., sign-out)
        Box(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu Icon",
                )
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Sign Out",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                        )
                    },
                    onClick = {
                        isMenuExpanded = false
                        onSignOut()
                    }
                )
            }
        }
    }
}

@Composable
private fun ScheduleContent(
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel(),
) {
    val appointments by viewModel.appointments.collectAsState()

    // Fetch appointments and doctors when this composable is launched
    LaunchedEffect(Unit) {
        viewModel.fetchDoctors()
        viewModel.fetchUserAppointmentsAndDoctors()
    }

    // Display a random appointment or a placeholder message if none exist
    val appointment = if (appointments.isNotEmpty()) appointments.random() else null

    if (appointment != null) {
        // Card showing details of an upcoming appointment
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = BluePrimary,
            tonalElevation = 1.dp,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)) {
                Text(
                    text = "Your Upcoming Appointment",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold, color = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                    textAlign = TextAlign.Start
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .height(1.dp)
                        .alpha(0.2f),
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Doctor's image
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(0.5.dp, Color.Gray, CircleShape),
                        painter = painterResource(id = appointment.doctor.imageRes),
                        contentDescription = "Doctor Image"
                    )

                    // Doctor's name and specialty
                    Column(
                        modifier = Modifier.padding(start = 12.dp).weight(1f)
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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                        .height(1.dp)
                        .alpha(0.2f),
                    color = Color.White
                )

                // Display appointment date and time
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
        // Placeholder for when no appointments exist
        Surface(
            modifier = Modifier.fillMaxWidth().height(150.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF5F5F5)
        ) {
            Box(
                modifier = Modifier.padding(24.dp),
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
fun DoctorList(
    navController: NavController, viewModel: DoctorViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val doctors by viewModel.doctors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Search bar for filtering doctors
        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
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

        // Display loading indicator or doctor list
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
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
            contentDescription = "Icon"
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
