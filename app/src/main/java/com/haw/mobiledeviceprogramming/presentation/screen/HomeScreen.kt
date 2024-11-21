package com.haw.mobiledeviceprogramming.presentation.screen

import DoctorViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.presentation.components.MenuHome
import com.haw.mobiledeviceprogramming.presentation.components.NearDoctorCard
import com.haw.mobiledeviceprogramming.presentation.components.ScheduleTimeContent
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.GraySecond
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.WhiteBackground
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import com.haw.mobiledeviceprogramming.presentation.utils.Doctor
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel() // Pass the ViewModel here
) {
    Surface(
        modifier = modifier
            .padding(top = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            // Header Content
            HeaderContent()

            Spacer(modifier = Modifier.height(32.dp))

            // Schedule Content
            ScheduleContent()

            //Search Bar
            SearchableDoctorList(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun HomeScreenWithNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "doctor_list"
    ) {
        // List Screen
        composable("doctor_list") {
            HomeScreen(navController = navController) // Pass navController here
        }

        // Details Screen
        composable(
            route = "doctor_details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType})
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            DoctorDetailsScreen(id = id)
        }
    }
}

@Composable
private fun HeaderContent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
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
                text = "Hi, Shidiq",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Image(
            modifier = Modifier.size(56.dp),
            painter = painterResource(id = R.drawable.img_header_content),
            contentDescription = "Image Header Content"
        )
    }
}

@Composable
private fun ScheduleContent(
    modifier: Modifier = Modifier,
    viewModel: DoctorViewModel = viewModel() // Use ViewModel to fetch doctors
) {
    val doctors by viewModel.doctors.collectAsState() // Observe the doctor list

    // Trigger fetching of doctors
    LaunchedEffect(Unit) {
        viewModel.fetchDoctors()
    }

    // Get a random doctor or show loading state
    val doctor = if (doctors.isNotEmpty()) doctors.random() else null

    if (doctor != null) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = doctor.imageRes),
                        contentDescription = "Image Doctor"
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = doctor.name,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = doctor.specialty,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Light,
                            color = GraySecond
                        )
                    }

                    Image(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Arrow Next",
                        colorFilter = ColorFilter.tint(color = Color.White)
                    )
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(1.dp)
                        .alpha(0.2f),
                    color = Color.White
                )

                // Schedule Time Content
                ScheduleTimeContent()
            }
        }
    } else {
        // Loading placeholder
        Text(
            text = "Loading...",
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            fontFamily = poppinsFontFamily,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun SearchableDoctorList(
    navController: NavController,
    viewModel: DoctorViewModel = viewModel()
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
                    text = "Search doctor or health issue",
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

        val filteredDoctors = if (searchQuery.isBlank()) doctors else {
            doctors.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }

        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(color = BluePrimary)
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
                    NearDoctorCard(doctor = doctor, navController = navController)
                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun HomeScreenPreview() {
//    HomeScreen()
//}