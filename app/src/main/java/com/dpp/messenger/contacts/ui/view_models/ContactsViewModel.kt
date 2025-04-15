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
import com.dpp.messenger.libs.handleRequest
import kotlinx.coroutines.launch

class ContactsViewModel(private val apiService: ApiService) : ViewModel() {

    private val _contacts = MutableLiveData<List<ContactResponse>>()
    val contacts: LiveData<List<ContactResponse>> get() = _contacts

    init {
        updateContacts()
    }

    fun updateContacts() {
        viewModelScope.launch {
            val response = handleRequest {
                apiService.getContacts()
            }

            when (response.code()) {
                200 -> {
                    response.body()?.let {
                        _contacts.value = it
                    }
                }

                -1 -> {

                }
            }
        }
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ContactsViewModel(
                        apiService = apiService
                    )
                }
            }
    }
}