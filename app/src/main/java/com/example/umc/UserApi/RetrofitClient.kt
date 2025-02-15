package com.example.umc.UserApi

import com.example.umc.UserApi.APi.GetUserApi
import com.example.umc.UserApi.APi.KakaoLoginApi
import com.example.umc.UserApi.APi.OtpApi
import com.example.umc.UserApi.APi.OtpValidationApi
import com.example.umc.UserApi.APi.UpdateUserApi
import com.example.umc.UserApi.APi.UserApi
import com.example.umc.model.service.ImageApiService
import com.example.umc.model.service.MealApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// swagger 연결 http
object RetrofitClient {
    private const val BASE_URL = "http://3.38.39.238:3000/"  // 여기에 API 서버 주소 입력

    val instance: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환
            .build()
            .create(UserApi::class.java)
    }


    val mealApiService : MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }

    val getApiService: GetUserApi by lazy {  // 반환 타입을 GetUserApi로 변경
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GetUserApi::class.java)  // GetUserApi로 변경
    }

    val updateUserApi: UpdateUserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UpdateUserApi::class.java)
    }

    val imageApiService: ImageApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageApiService::class.java)
    }

    val otpApi: OtpApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OtpApi::class.java)
    }

    val otpValidationApi: OtpValidationApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OtpValidationApi::class.java)
    }

    val kakaoLoginApi: KakaoLoginApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoLoginApi::class.java)
    }
}