package com.haw.mobiledeviceprogramming.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.haw.mobiledeviceprogramming.presentation.utils.Navigation
import com.haw.mobiledeviceprogramming.navigation.screen.BottomNavItemScreen
import com.app.mobiledeviceprogramming.ui.theme.BluePrimary
import com.app.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.app.mobiledeviceprogramming.ui.theme.poppinsFontFamily

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    var navigationSelectedItem by remember { mutableIntStateOf(0) }
    val bottomNavigationItems = listOf(
        BottomNavItemScreen.Home,
        BottomNavItemScreen.Schedule,
        BottomNavItemScreen.Medicine,
        BottomNavItemScreen.Profile
    )

    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                bottomNavigationItems.forEachIndexed { index, bottomNavItemScreen ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = BluePrimary,
                            selectedIconColor = BluePrimary,
                            unselectedTextColor = PurpleGrey,
                            unselectedIconColor = PurpleGrey
                        ),
                        selected = index == navigationSelectedItem,
                        icon = {
                            Icon(
                                painter = painterResource(id = bottomNavItemScreen.icon),
                                contentDescription = "Icon Bottom Nav"
                            )
                        },
                        label = {
                            Text(
                                text = bottomNavItemScreen.title,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF63B4FF)
                            )
                        },
                        alwaysShowLabel = index == navigationSelectedItem,
                        onClick = {
                            navigationSelectedItem = index
                            navController.navigate(bottomNavItemScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) { paddingValues ->
        Navigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}
