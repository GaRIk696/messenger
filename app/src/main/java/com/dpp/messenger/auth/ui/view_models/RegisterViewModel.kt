package com.dpp.messenger.auth.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dpp.messenger.data.ApiService
import com.dpp.messenger.data.models.LoginResponse
import com.dpp.messenger.data.models.RegisterRequest
import com.dpp.messenger.data.models.errors.err
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val apiService: ApiService) : ViewModel() {

    private val _registerResponse = MutableLiveData<LoginResponse?>()
    val registerResponse: LiveData<LoginResponse?> = _registerResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _validationErrors = MutableLiveData<Triple<String?, String?, String?>>()
    val validationErrors: LiveData<Triple<String?, String?, String?>> = _validationErrors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(request: RegisterRequest) {
        _isLoading.value = true
        apiService.register(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else if (response.code() == 422) {
                    handleValidationErrors(response)
                } else {
                    _errorMessage.value = "Registration failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Network error: ${t.message}"
            }
        })
    }

    private fun handleValidationErrors(response: Response<LoginResponse>) {
        val gson = Gson()
        val type = object : TypeToken<err>() {}.type
        val errorResponse: err? = gson.fromJson(response.errorBody()?.charStream(), type)

        val loginError = errorResponse?.errors?.Login?.firstOrNull()
        val nameError = errorResponse?.errors?.Name?.firstOrNull()
        val passwordError = errorResponse?.errors?.Password?.firstOrNull()

        _validationErrors.value = Triple(loginError, nameError, passwordError)
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    RegisterViewModel(apiService)
                }
            }
    }
}