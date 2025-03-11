package com.messenger.messenger.contacts.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.messenger.messenger.data.ApiService

class ContactsViewModel(val apiService: ApiService): ViewModel() {

    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ContactsViewModel(
                        apiService = apiService
                    )
                }
            }}
}