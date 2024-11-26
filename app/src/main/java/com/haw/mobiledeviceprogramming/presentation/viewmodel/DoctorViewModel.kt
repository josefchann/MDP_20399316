import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.haw.mobiledeviceprogramming.R
import com.haw.mobiledeviceprogramming.presentation.crud.Appointment
import com.haw.mobiledeviceprogramming.presentation.utils.Doctor
import com.haw.mobiledeviceprogramming.presentation.utils.DoctorRepository
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
        if (_isLoading.value) return // Avoid duplicate fetches
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



    // Get a specific doctor by index (wraps around if index exceeds list size)
    fun getDoctorForIndex(index: Int): Doctor? {
        return if (_doctors.value.isNotEmpty()) {
            _doctors.value[index % _doctors.value.size] // Cycle through doctors if needed
        } else {
            null
        }
    }

    fun getDoctorById(id: Int): Doctor? {
        Log.d("DoctorViewModel", "Searching for doctor with ID: $id in list: ${_doctors.value}")
        return _doctors.value.find { it.id == id }
    }


}

