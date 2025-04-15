package com.dpp.messenger.contacts.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dpp.messenger.data.ApiService
import com.dpp.messenger.data.models.UserResponse
import com.dpp.messenger.libs.debounce
import com.dpp.messenger.libs.handleRequest
import kotlinx.coroutines.launch

class UserSearchViewModel(private val apiService: ApiService) : ViewModel() {
    private val _users = MutableLiveData<List<UserResponse>>()
    val users: LiveData<List<UserResponse>> get() = _users

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    val searchUsers: (String) -> Unit = debounce(300L, viewModelScope, this::onUsersSearch)
    private fun onUsersSearch(query: String) {
        viewModelScope.launch {

            val response = handleRequest {
                apiService.userSearch(query)
            }

            when (response.code()) {
                200 -> {

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
                    UserSearchViewModel(
                        apiService = apiService
                    )
                }
            }
    }
}