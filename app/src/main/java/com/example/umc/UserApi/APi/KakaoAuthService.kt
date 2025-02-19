package com.example.umc.UserApi.APi

import com.example.umc.UserApi.Request.KakaoAuthRequest
import com.example.umc.UserApi.Response.AuthResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface KakaoAuthService {
    @POST("auth/kakao")
    suspend fun loginWithKakao(
        @Body kakaoAuthRequest: KakaoAuthRequest
    ): Response<AuthResponse>

    companion object {
        // create() 메서드가 KakaoAuthService를 반환하도록 수정
        fun create(): KakaoAuthService {
            return Retrofit.Builder()
                .baseUrl("http://3.38.39.238:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                // KakaoAuthService로 수정
                .create(KakaoAuthService::class.java)
        }
    }
}