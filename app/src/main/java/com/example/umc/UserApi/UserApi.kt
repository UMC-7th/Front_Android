package com.example.umc.UserApi


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("api/v1/users/signup")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST("api/v1/users/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}