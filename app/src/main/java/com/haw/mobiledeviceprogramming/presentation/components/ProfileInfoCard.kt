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
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily

@Composable
fun ProfileInfoCard(
    condition: String,
    date: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        color = Color.White,
        tonalElevation = 0.5.dp,
        shadowElevation = 0.2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = condition,  // Use passed data
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = poppinsFontFamily
            )
            Text(
                text = date,  // Use passed data
                fontSize = 14.sp,
                color = BluePrimary,
                fontFamily = poppinsFontFamily
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun ProfileInfoCardPreview() {
//    ProfileInfoCard()
//}
