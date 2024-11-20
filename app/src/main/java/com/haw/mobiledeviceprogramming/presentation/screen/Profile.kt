package com.haw.mobiledeviceprogramming.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haw.mobiledeviceprogramming.presentation.components.ProfileInfoCard
import com.haw.mobiledeviceprogramming.presentation.components.SettingsCard
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        // Title Section
        ProfileTitle()

        // Profile Information Section
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Replace with items relevant to Profile
            items(3) {
                ProfileInfoCard() // Shows user's profile information
            }
        }
    }
}

@Composable
private fun ProfileTitle(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        text = "Medical History",
        fontFamily = poppinsFontFamily,
        color = BluePrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.Start
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}
