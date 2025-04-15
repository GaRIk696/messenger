package com.dpp.messenger.settings.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dpp.messenger.data.ApiService
import com.dpp.messenger.libs.handleRequest
import com.dpp.messenger.settings.models.SettingsScreenState
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SettingsViewModel(private val apiService: ApiService) : ViewModel() {
    private val _state = MutableLiveData<SettingsScreenState>(SettingsScreenState.Loading)
    val state: LiveData<SettingsScreenState> get() = _state

    private fun renderState(newState: SettingsScreenState){
        _state.value = newState
    }

    init {
        loadingUser()
    }

    fun loadingUser() {
        viewModelScope.launch {
            val response = handleRequest {
                apiService.getUser()
            }

            when (response.code()) {
                200 -> {
                    renderState(SettingsScreenState.Content(response.body()!!, null))
                }

                -1 -> {
                    renderState(SettingsScreenState.Error(response.errorBody()?.string() ?: "Ошибка сети"))
                }
            }
        }
    }

    fun uploadUserAvatar(file: MultipartBody.Part){
        viewModelScope.launch {
            val response = handleRequest {
                apiService.updateAvatar(file)
            }

            when (response.code()) {
                200 -> {
                    renderState(SettingsScreenState.Content(null, response.body()!!))
                }

                -1 -> {
                    renderState(SettingsScreenState.Error(response.errorBody()?.string() ?: "Ошибка сети"))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val response = handleRequest {
                apiService.logout()
            }

            when (response.code()) {
                200 -> {
                    renderState(SettingsScreenState.NavAuth)
                }

                -1 -> {
                    renderState(SettingsScreenState.Error(response.errorBody()?.string() ?: "Ошибка сети"))
                }
            }
        }
    }


    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(
                        apiService = apiService
                    )
                }
            }
    }
}