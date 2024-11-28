package com.haw.mobiledeviceprogramming.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.PurpleGrey
import com.haw.mobiledeviceprogramming.ui.theme.TextColorTitle
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import com.haw.mobiledeviceprogramming.presentation.utils.Medicine
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun MedicineCard(
    modifier: Modifier = Modifier,
    medicine: Medicine,
    onDetailClick: (Medicine) -> Unit = {}, // Callback for detail action
    onDeleteClick: (Medicine) -> Unit = {} // Callback for delete action
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // Space between content and trash icon
            ) {
                Row {
                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = medicine.imageRes),
                        contentDescription = "Image of ${medicine.medicineName}"
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = medicine.medicineName,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextColorTitle
                        )

                        Text(
                            text = medicine.medicineUsage,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp,
                            color = PurpleGrey
                        )
                    }
                }

                // Trash can icon for deleting the medicine
                IconButton(
                    onClick = { onDeleteClick(medicine) }, // Trigger delete action
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Replace with desired icon
                        contentDescription = "Delete", // Accessibility description
                        tint = Color.Gray // Optional: Customize icon color
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .alpha(0.2f),
                color = Color.Gray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Usage",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = PurpleGrey
                    )
                    Text(
                        text = medicine.medicineUsage,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = TextColorTitle
                    )
                }

                Column {
                    Text(
                        text = "Restock Date",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = PurpleGrey
                    )
                    Text(
                        text = medicine.restockDate,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = TextColorTitle
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .alpha(0.2f),
                color = Color.Gray
            )

            // Remove Medicine Button
            Button(
                onClick = { onDeleteClick(medicine) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFEFEF), // Light gray for a subtle look
                    contentColor = Color.Black // Black text for contrast
                ),
                shape = RoundedCornerShape(8.dp) // Slightly rounded corners
            ) {
                Text(
                    text = "Remove Medicine",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

        }
    }
}


