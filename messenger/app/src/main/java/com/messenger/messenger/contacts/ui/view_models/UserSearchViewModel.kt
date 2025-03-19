package com.messenger.messenger.contacts.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.messenger.messenger.data.ApiService
import com.messenger.messenger.data.models.UserResponse
import com.messenger.messenger.libs.debounce
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSearchViewModel(private  val apiService: ApiService) : ViewModel() {
    private val _users = MutableLiveData<List<UserResponse>>()
    val users : LiveData<List<UserResponse>> get() =_users

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get()=_error

    val searchUsers: (String) -> Unit = debounce(300L, viewModelScope, this::onUsersSearch)
    private fun onUsersSearch(query: String) {
        apiService.userSearch(query).enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                if (response.isSuccessful) {

                    _users.value = response.body()
                    _error.value = null
                } else {

                    _error.value = "Ошибка при поиске пользователей: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {

                _error.value = "Ошибка сети: ${t.message}"
            }
        })
    }
    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory= viewModelFactory {
            initializer {
                UserSearchViewModel(
                    apiService=apiService
                )
            }
        }
    }
}