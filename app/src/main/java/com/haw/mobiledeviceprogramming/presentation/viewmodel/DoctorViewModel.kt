import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haw.mobiledeviceprogramming.presentation.utils.Doctor
import com.haw.mobiledeviceprogramming.presentation.utils.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoctorViewModel : ViewModel() {
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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

    // Get a specific doctor by index (wraps around if index exceeds list size)
    fun getDoctorForIndex(index: Int): Doctor? {
        return if (_doctors.value.isNotEmpty()) {
            _doctors.value[index % _doctors.value.size] // Cycle through doctors if needed
        } else {
            null
        }
    }
}

