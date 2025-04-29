package com.dpp.messenger.contacts.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dpp.messenger.data.ApiService
import com.dpp.messenger.data.models.ContactResponse
import kotlinx.coroutines.launch

class ContactsViewModel(private val apiService: ApiService) : ViewModel() {

    private val _contacts = MutableLiveData<List<ContactResponse>>()
    val contacts: LiveData<List<ContactResponse>> = _contacts

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadContacts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getContacts()
                if (response.isSuccessful) {
                    _contacts.value = response.body()
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

    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ContactsViewModel(apiService)
                }
            }
    }
}