package com.example.umc.UserApi.APi


import com.example.umc.UserApi.Request.LoginRequest
import com.example.umc.UserApi.Response.LoginResponse
import com.example.umc.UserApi.Request.SignUpRequest
import com.example.umc.UserApi.Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
//엔드포인트 연결
interface UserApi {
    @POST("api/v1/users/signup")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST("api/v1/users/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}