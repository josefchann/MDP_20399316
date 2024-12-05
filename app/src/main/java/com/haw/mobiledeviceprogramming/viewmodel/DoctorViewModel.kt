import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.viewmodel.Appointment
import com.haw.mobiledeviceprogramming.utils.Doctor
import com.haw.mobiledeviceprogramming.utils.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DoctorViewModel : ViewModel() {
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _filteredDoctors = MutableStateFlow<List<Doctor>>(emptyList())
    val filteredDoctors: StateFlow<List<Doctor>> = _filteredDoctors.asStateFlow()

    fun fetchDoctors() {
        if (_isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fetchedDoctors = DoctorRepository.fetchDoctors()
                _doctors.value = fetchedDoctors
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUserAppointmentsAndDoctors() {
        _isLoading.value = true

        val currentUserUuid = auth.currentUser?.uid ?: return

        firestore.collection("appointments")
            .whereEqualTo("userUuid", currentUserUuid)
            .get()
            .addOnSuccessListener { documents ->
                val appointments = documents.mapNotNull { document ->
                    try {
                        val appointment = document.toObject(Appointment::class.java)

                        // Map doctor details dynamically
                        val doctorData = document.get("doctor") as? Map<String, Any>
                        val doctor = doctorData?.let {
                            Doctor(
                                name = it["name"] as? String ?: "",
                                specialty = it["specialty"] as? String ?: "",
                                imageRes = (it["imageRes"] as? Long)?.toInt() ?: R.drawable.ic_doctor,
                                openingTime = it["openingTime"] as? String ?: "",
                                id = (it["id"] as? Long)?.toInt() ?: 0,
                                education = it["education"] as? String ?: "",
                                consultationFee = (it["consultationFee"] as? Long)?.toInt() ?: 0,
                                location = it["location"] as? String ?: "",
                            )
                        }

                        appointment.copy(doctor = doctor ?: Doctor())
                    } catch (e: Exception) {
                        Log.e("DoctorViewModel", "Error mapping appointment: ${e.message}")
                        null
                    }
                }

                _appointments.value = appointments
                _isLoading.value = false
            }
            .addOnFailureListener { exception ->
                Log.e("DoctorViewModel", "Error fetching appointments: ${exception.message}")
                _isLoading.value = false
            }
    }

    fun getDoctorById(id: Int): Doctor? {
        Log.d("DoctorViewModel", "Searching for doctor with ID: $id in list: ${_doctors.value}")
        return _doctors.value.find { it.id == id }
    }

    fun deleteAppointment(appointmentId: Int, onSuccess: () -> Unit = {}, onError: (Exception) -> Unit = {}) {
        firestore.collection("appointments")
            .whereEqualTo("doctor.id", appointmentId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val updatedAppointments = _appointments.value.toMutableList()

                for (document in querySnapshot.documents) {
                    // Remove the document reference
                    document.reference.delete()
                        .addOnSuccessListener {
                            // Remove the corresponding appointment locally
                            val appointmentToRemove = updatedAppointments.find {
                                it.doctor.id == appointmentId
                            }
                            if (appointmentToRemove != null) {
                                updatedAppointments.remove(appointmentToRemove)
                                _appointments.value = updatedAppointments
                            }
                            Log.d("DoctorViewModel", "Successfully deleted appointment with id: $appointmentId")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e("DoctorViewModel", "Error deleting document: ${e.message}")
                            onError(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("DoctorViewModel", "Error fetching appointments: ${e.message}")
                onError(e)
            }
    }

}

