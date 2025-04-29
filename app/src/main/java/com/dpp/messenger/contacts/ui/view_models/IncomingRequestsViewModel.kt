import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dpp.messenger.data.ApiService
import com.dpp.messenger.data.models.AcceptContactRequest
import com.dpp.messenger.data.models.ContactRequest
import com.dpp.messenger.data.models.DeclineContactRequest
import kotlinx.coroutines.launch

class IncomingRequestsViewModel(private val apiService: ApiService) : ViewModel() {
    private val _incomingRequests = MutableLiveData<List<ContactRequest>>()
    val incomingRequests: LiveData<List<ContactRequest>> = _incomingRequests

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadIncomingRequests() {
        viewModelScope.launch {
            try {
                val response = apiService.getIncomingRequests()
                if (response.isSuccessful) {
                    _incomingRequests.value = response.body()
                } else {

                    _incomingRequests.value = emptyList()
                }
            } catch (e: Exception) {

                _incomingRequests.value = emptyList()
            }
        }
    }

    fun acceptContactRequest(requestId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.acceptContactRequest(AcceptContactRequest(requestId))
                if (response.isSuccessful) {
                    loadIncomingRequests()

                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun declineContactRequest(requestId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                apiService.declineContactRequest(DeclineContactRequest(requestId))
                loadIncomingRequests()
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    IncomingRequestsViewModel(apiService)
                }
            }
    }
}