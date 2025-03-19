package com.messenger.messenger.data

import com.messenger.messenger.data.models.ContactResponse
import com.messenger.messenger.data.models.LoginRequest
import com.messenger.messenger.data.models.LoginResponse
import com.messenger.messenger.data.models.RegisterRequest
import com.messenger.messenger.data.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<LoginResponse>

    @POST("/auth/logout")
    fun logout(): Call<Void>

    @GET("/users/user")
    fun getUser(): Call<UserResponse>

    @GET("contacts")
    suspend fun getContacts(): Call<List<ContactResponse>>

    @GET("/users/search")
    fun userSearch(@Query("search") search: String): Call<List<UserResponse>>
}