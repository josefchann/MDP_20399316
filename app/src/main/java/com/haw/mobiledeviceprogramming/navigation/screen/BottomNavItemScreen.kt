package com.haw.mobiledeviceprogramming.navigation.screen

import com.haw.mobiledeviceprogramming.R

sealed class BottomNavItemScreen(val route: String, val icon: Int, val title: String) {

    data object Home : BottomNavItemScreen(
        route = "home_screen",
        icon = R.drawable.ic_bottom_home,
        title = "Home"
    )

    data object Schedule : BottomNavItemScreen(
        route = "schedule_screen",
        icon = R.drawable.ic_bottom_schedule,
        title = "Schedule"
    )

    data object Medicine : BottomNavItemScreen(
        route = "medicine_screen",
        icon = R.drawable.ic_medicine,
        title = "Medication"
    )

    data object Profile : BottomNavItemScreen(
        route = "profile_screen",
        icon = R.drawable.ic_bottom_profile,
        title = "Profile"
    )

    data object DoctorDetails : BottomNavItemScreen(
        route = "doctor_details",
        icon = R.drawable.ic_bottom_profile,
        title = "Doctor Details"
    )
}