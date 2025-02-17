package com.example.umc.UserApi

import android.content.Context
import com.example.umc.UserApi.APi.GetUserApi
import com.example.umc.UserApi.APi.KakaoLoginApi
import com.example.umc.UserApi.APi.OtpApi
import com.example.umc.UserApi.APi.OtpValidationApi
import com.example.umc.UserApi.APi.UpdateUserApi
import com.example.umc.UserApi.APi.UserApi
import com.example.umc.model.service.ImageApiService
import com.example.umc.model.service.MealApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// swagger 연결 http

object RetrofitClient {
    private const val BASE_URL = "http://3.38.39.238:3000/"  // 여기에 API 서버 주소 입력
    private lateinit var okHttpClient: OkHttpClient

    // ✅ SharedPreferences 초기화 (Application에서 호출 필요)
    fun init(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // 인증 헤더 추가
            .connectTimeout(30, TimeUnit.SECONDS) // 연결 타임아웃 설정
            .readTimeout(30, TimeUnit.SECONDS) // 읽기 타임아웃 설정
            .writeTimeout(30, TimeUnit.SECONDS) // 쓰기 타임아웃 설정
            .build()
    }

    val instance: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환
            .client(okHttpClient)
            .build()
            .create(UserApi::class.java)
    }

    val mealApiService: MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MealApiService::class.java)
    }

    val getApiService: GetUserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(GetUserApi::class.java)
    }

    val updateUserApi: UpdateUserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(UpdateUserApi::class.java)
    }

    val imageApiService: ImageApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ImageApiService::class.java)
    }

    val otpApi: OtpApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(OtpApi::class.java)
    }

    val otpValidationApi: OtpValidationApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(OtpValidationApi::class.java)
    }

    val kakaoLoginApi: KakaoLoginApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(KakaoLoginApi::class.java)
    }
}