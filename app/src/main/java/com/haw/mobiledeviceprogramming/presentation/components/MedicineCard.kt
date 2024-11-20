package com.haw.mobiledeviceprogramming.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.TextColorTitle
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import com.haw.mobiledeviceprogramming.presentation.utils.Medicine
import com.haw.mobiledeviceprogramming.presentation.utils.getMedicine

@Composable
fun MedicineCard(
    modifier: Modifier = Modifier,
    medicine: Medicine = getMedicine()  // Use a random medicine if no parameter is passed
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        tonalElevation = 0.5.dp,
        shadowElevation = 0.2.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Row {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = medicine.imageRes),
                    contentDescription = "Image of ${medicine.name}"
                )

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
                ) {
                    Text(
                        text = medicine.name,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TextColorTitle
                    )

                    Text(
                        text = medicine.description,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Light,
                        color = PurpleGrey
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .alpha(0.1f),
                color = Color.Gray
            )

            MedicationContent(contentColor = PurpleGrey)

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF63B4FF).copy(alpha = 0.1f)),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "Detail",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = BluePrimary
                )
            }
        }
    }
}



@Preview
@Composable
private fun MedicineConsumptionCardPreview() {
    MedicineCard()
}