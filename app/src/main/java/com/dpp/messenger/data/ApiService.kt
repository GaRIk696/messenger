package com.dpp.messenger.data

import com.dpp.messenger.data.models.ContactResponse
import com.dpp.messenger.data.models.LoginRequest
import com.dpp.messenger.data.models.LoginResponse
import com.dpp.messenger.data.models.RegisterRequest
import com.dpp.messenger.data.models.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<LoginResponse>

    @POST("/auth/logout")
    suspend fun logout(): Response<Unit>

    @GET("/users/user")
    suspend fun getUser(): Response<UserResponse>

    @GET("contacts")
    suspend fun getContacts(): Response<List<ContactResponse>>

    @GET("/users/search")
    suspend fun userSearch(@Query("search") search: String): Response<UserResponse>

    @Multipart
    @PATCH("/users/update/avatar")
    suspend fun updateAvatar(@Part file: MultipartBody.Part): Response<String>
}