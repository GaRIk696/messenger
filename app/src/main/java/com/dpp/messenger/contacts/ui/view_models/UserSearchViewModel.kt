package com.dpp.messenger.contacts.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dpp.messenger.data.ApiService
import com.dpp.messenger.data.models.AddContactRequest
import com.dpp.messenger.data.models.UserResponse
import com.dpp.messenger.libs.debounce
import com.dpp.messenger.libs.handleRequest
import kotlinx.coroutines.launch

class UserSearchViewModel(private val apiService: ApiService) : ViewModel() {
    private val _users = MutableLiveData<List<UserResponse>>(emptyList())
    val users: LiveData<List<UserResponse>> get() = _users

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _status = MutableLiveData<String?>(null)
    val status: LiveData<String?> = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Оптимизированный поиск с debounce и минимальной длиной запроса
    val searchUsers: (String) -> Unit = debounce(300L, viewModelScope) { query ->
        onUsersSearch(query)
    }

    private fun onUsersSearch(query: String) {

        _isLoading.value = true
        viewModelScope.launch {
            val response = handleRequest {
                apiService.userSearch(query, 100)
            }

            when (response.code()) {
                200 -> {
                    _users.value = response.body() ?: emptyList()
                    _error.value = null
                }

                404 -> {
                    _users.value = emptyList()
                    _error.value = "Ничего не найдено"
                }

                else -> {
                    _error.value = "Ошибка поиска: ${response.code()}"
                }
            }
        }
    }

    fun addContact(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val request = AddContactRequest(userId = userId)
            val response = handleRequest {
                apiService.addContact(request)
            }

            if (response.isSuccessful) {
                _status.value = "Контакт успешно добавлен"
            }
        }
    }

    fun clearStatus() {
        _status.value = null
        _error.value = null
    }

    fun clearSearchResults() {
        _users.value = emptyList()
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    UserSearchViewModel(apiService = apiService)
                }
            }
    }
}