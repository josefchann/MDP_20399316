package com.haw.mobiledeviceprogramming.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily

@Composable
fun SettingsCard(modifier: Modifier = Modifier, title: String) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsCardPreview() {
    SettingsCard(title = "Account Settings")
}
