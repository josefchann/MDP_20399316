package com.haw.mobiledeviceprogramming.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.haw.mobiledeviceprogramming.presentation.components.MedicineCard
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.haw.mobiledeviceprogramming.presentation.utils.Medicine
import com.haw.mobiledeviceprogramming.presentation.viewmodel.MedicineViewModel
import java.util.Calendar

@Composable
fun MedicineScreen(
    modifier: Modifier = Modifier,
    viewModel: MedicineViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) } // Flag for delete confirmation
    var medicineToDelete by remember { mutableStateOf<Medicine?>(null) } // Medicine to delete
    val userUuid = FirebaseAuth.getInstance().currentUser?.uid
    val medicines by viewModel.medicines.collectAsState() // Observing medicines from ViewModel

    // Fetch medicines for the current user when the screen is composed
    LaunchedEffect(userUuid) {
        userUuid?.let {
            viewModel.fetchMedicinesForUser(it)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Header row
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(1) {
                CategorySchedule()
            }
        }

        // Display "No medicines available" if list is empty
        if (medicines.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Medication Available.",
                    fontFamily = poppinsFontFamily,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // List of medicines
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(medicines) { medicine ->
                    MedicineCard(
                        medicine = medicine,
                        onDetailClick = { selectedMedicine ->
                            println("Detail clicked for: ${selectedMedicine.medicineName}")
                        },
                        onDeleteClick = { selectedMedicine ->
                            // Show delete confirmation dialog
                            medicineToDelete = selectedMedicine
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Floating Action Button
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = BluePrimary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Medicine")
        }
    }

    // Add Medicine Dialog
    if (showDialog) {
        AddMedicineDialog(
            onDismiss = { showDialog = false },
            onSubmit = { medicineName, medicineUsage, restockDate ->
                if (userUuid != null) {
                    viewModel.addMedicineToDatabase(
                        medicineName,
                        medicineUsage,
                        restockDate,
                        userUuid,
                        onSuccess = {
                            showDialog = false
                            println("Medicine added successfully!")
                            // Refresh the list after adding a medicine
                            viewModel.fetchMedicinesForUser(userUuid)
                        },
                        onError = { exception ->
                            println("Error adding medicine: ${exception.message}")
                        }
                    )
                }
            }
        )
    }

    // Delete Medicine Confirmation Dialog
    if (showDeleteDialog && medicineToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Medicine", fontFamily = poppinsFontFamily, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Are you sure you want to delete ${medicineToDelete!!.medicineName}?",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        medicineToDelete?.id?.let {
                            viewModel.deleteMedicineById(
                                medicineId = it,
                                onSuccess = {
                                    showDeleteDialog = false
                                    println("Medicine deleted successfully!")
                                    // Refresh the list after deleting the medicine
                                    viewModel.fetchMedicinesForUser(userUuid!!)
                                },
                                onError = { exception ->
                                    println("Error deleting medicine: ${exception.message}")
                                }
                            )
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun AddMedicineDialog(
    onDismiss: () -> Unit,
    onSubmit: (medicineName: String, medicineUsage: String, restockDate: String ) -> Unit
) {
    var medicineName by remember { mutableStateOf(TextFieldValue("")) }
    var medicineUsage by remember { mutableStateOf(TextFieldValue("")) }
    var restockDate by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Open DatePickerDialog
    fun showDatePicker() {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                restockDate = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add Medicine", fontFamily = poppinsFontFamily, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Medicine Name Input
                Column {
                    Text("Medicine Name", color = Color.Gray, fontSize = 14.sp)
                    BasicTextField(
                        value = medicineName,
                        onValueChange = { medicineName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                }

                // Medicine Usage Input
                Column {
                    Text("Medicine Usage", color = Color.Gray, fontSize = 14.sp)
                    BasicTextField(
                        value = medicineUsage,
                        onValueChange = { medicineUsage = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                }

                // Restock Date Selector
                Column {
                    Text("Restock Date", color = Color.Gray, fontSize = 14.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                            .clickable { showDatePicker() }
                    ) {
                        Text(
                            text = if (restockDate.isEmpty()) "Select Restock Date" else restockDate,
                            color = if (restockDate.isEmpty()) Color.Gray else Color.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSubmit(medicineName.text, medicineUsage.text, restockDate) }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )

}



@Composable
private fun CategorySchedule(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        text = "Medications",
        fontFamily = poppinsFontFamily,
        color = BluePrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.Start
    )
}


