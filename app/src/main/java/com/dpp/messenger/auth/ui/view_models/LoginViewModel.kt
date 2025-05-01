package com.dpp.messenger.auth.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dpp.messenger.data.ApiService
import com.dpp.messenger.data.models.LoginRequest
import com.dpp.messenger.data.models.LoginResponse
import com.dpp.messenger.data.models.errors.err
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val apiService: ApiService) : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _validationErrors = MutableLiveData<Pair<String?, String?>>()
    val validationErrors: LiveData<Pair<String?, String?>> = _validationErrors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(loginRequest: LoginRequest) {
        _isLoading.value = true
        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    handleErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "Unknown error occurred"
            }
        })
    }

    private fun handleErrorResponse(response: Response<LoginResponse>) {
        when (response.code()) {
            422 -> {
                val gson = Gson()
                val type = object : TypeToken<err>() {}.type
                val errorResponse: err? = gson.fromJson(response.errorBody()!!.charStream(), type)
                val loginError = errorResponse?.errors?.Login?.firstOrNull()
                val passwordError = errorResponse?.errors?.Password?.firstOrNull()
                _validationErrors.value = Pair(loginError, passwordError)
            }
            400 -> {
                _errorMessage.value = response.errorBody()?.string() ?: "Invalid request"
            }
            else -> {
                _errorMessage.value = "Error: ${response.code()}"
            }
        }
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    LoginViewModel(apiService)
                }
            }
    }
}