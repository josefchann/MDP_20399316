package com.haw.mobiledeviceprogramming.presentation.screen

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.haw.mobiledeviceprogramming.presentation.components.ProfileInfoCard
import com.haw.mobiledeviceprogramming.utils.MedicalHistory
import com.haw.mobiledeviceprogramming.viewmodel.ProfileViewModel
import com.haw.mobiledeviceprogramming.ui.theme.BluePrimary
import com.haw.mobiledeviceprogramming.ui.theme.poppinsFontFamily
import java.util.Calendar

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val profileViewModel: ProfileViewModel = viewModel()
    val profiles by profileViewModel.profiles.observeAsState(emptyList())
    val openDialog = remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var profileToDelete by remember { mutableStateOf<String?>(null) }
    val userUuid = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        userUuid?.let { profileViewModel.fetchProfiles(it) }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ProfileTitle()

        if (profiles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Medical History",
                    fontFamily = poppinsFontFamily,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(profiles) { profile ->
                    val documentId = profile["id"] as? String
                    ProfileInfoCard(
                        condition = profile["condition"] as? String ?: "Unknown Condition",
                        date = profile["date"] as? String ?: "Unknown Date",
                        onDelete = {
                            profileToDelete = documentId
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { openDialog.value = true },
            containerColor = BluePrimary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Profile Info")
        }
    }

    if (openDialog.value) {
        AddProfileInfoDialog(
            onDismiss = { openDialog.value = false },
            onSubmit = { medicalHistory ->
                userUuid?.let { profileViewModel.addProfileInfo(medicalHistory, it) }
                openDialog.value = false
            }
        )
    }

    if (showDeleteDialog && profileToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Medical History", fontFamily = poppinsFontFamily, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Are you sure you want to delete this medical history?",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        userUuid?.let {
                            profileToDelete?.let { id ->
                                profileViewModel.deleteProfileInfo(id, it)
                                profileToDelete = null
                                showDeleteDialog = false
                            }
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

@Composable
fun AddProfileInfoDialog(
    onDismiss: () -> Unit,
    onSubmit: (medicalHistory: MedicalHistory) -> Unit
) {
    var profileInfo by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf("") }

    // Set up date picker dialog
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Function to show the date picker dialog
    fun showDatePicker() {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add Medical History", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Description Input
                Column {
                    Text("Medical Condition", color = Color.Gray, fontSize = 14.sp)
                    BasicTextField(
                        value = profileInfo,
                        onValueChange = { profileInfo = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                }

                // Date Picker
                Column {
                    Text("Select Date", color = Color.Gray, fontSize = 14.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                            .clickable { showDatePicker() }
                    ) {
                        Text(
                            text = if (selectedDate.isEmpty()) "Select Date" else selectedDate,
                            color = if (selectedDate.isEmpty()) Color.Gray else Color.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (profileInfo.text.isNotEmpty() && selectedDate.isNotEmpty()) {
                    val medicalHistory = MedicalHistory(
                        medicalCondition = profileInfo.text,
                        date = selectedDate
                    )
                    onSubmit(medicalHistory)
                }
            }) {
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
