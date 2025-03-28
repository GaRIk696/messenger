package com.dpp.messenger.contacts.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dpp.messenger.data.ApiService
import com.dpp.messenger.data.models.ContactResponse

class ContactsViewModel(private val apiService: ApiService) : ViewModel() {

    private val _contacts = MutableLiveData<List<ContactResponse>>()
    val contacts: LiveData<List<ContactResponse>> get() = _contacts

    fun updateContacts(newContactsList: List<ContactResponse>) {
        _contacts.value = newContactsList
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ContactsViewModel(
                    apiService = apiService
                )
            }
        }
    }
}