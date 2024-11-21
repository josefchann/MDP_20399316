package com.haw.mobiledeviceprogramming.presentation.utils
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.haw.mobiledeviceprogramming.navigation.screen.BottomNavItemScreen
import com.haw.mobiledeviceprogramming.presentation.screen.DoctorDetailsScreen
import com.haw.mobiledeviceprogramming.presentation.screen.HomeScreen
import com.haw.mobiledeviceprogramming.presentation.screen.HomeScreenWithNavigation
import com.haw.mobiledeviceprogramming.presentation.screen.ScheduleScreen
import com.haw.mobiledeviceprogramming.presentation.screen.MedicineScreen
import com.haw.mobiledeviceprogramming.presentation.screen.ProfileScreen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = BottomNavItemScreen.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = BottomNavItemScreen.Home.route) {
            HomeScreenWithNavigation()
        }
        composable(route = BottomNavItemScreen.Schedule.route) {
            ScheduleScreen()
        }
        composable(route = BottomNavItemScreen.Medicine.route) {
            MedicineScreen()
        }
        composable(route = BottomNavItemScreen.Profile.route) {
            ProfileScreen()
        }
        composable(route = BottomNavItemScreen.DoctorDetails.route) {
            DoctorDetailsScreen(id = id)
        }
    }
}

