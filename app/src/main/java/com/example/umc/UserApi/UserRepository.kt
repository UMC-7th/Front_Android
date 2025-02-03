package com.example.umc.UserApi

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val api = RetrofitClient.instance

    // SharedPreferences에 accessToken 저장
    companion object {
        private const val PREF_NAME = "UserPreferences"
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"

        fun saveAuthToken(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
        }

        fun getAuthToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        }
    }


    // 회원가입 처리
    fun signUp(request: SignUpRequest, callback: (Boolean, String) -> Unit) {
        api.signUp(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    callback(result?.success ?: false, result?.message ?: "응답 없음")
                } else {
                    callback(false, "서버 오류")
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback(false, "네트워크 오류: ${t.message}")
            }
        })
    }

    // 로그인 요청을 Call<LoginResponse>로 반환 (callback 제거)
    fun login(email: String, password: String): Call<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }


    suspend fun getUserProfile(context: Context): UserProfileResponse? {
        val accessToken = getAuthToken(context) // SharedPreferences에서 토큰 가져오기
        if (accessToken.isNullOrEmpty()) {
            Log.e("UserRepository", "액세스 토큰이 없습니다.")
            return null
        }

        Log.d("UserRepository", "현재 전달된 토큰: $accessToken")

        return try {
            val response = api.getUserProfile("Bearer $accessToken") // Authorization 헤더에 Bearer 토큰 추가
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                Log.e("UserRepository", "서버 응답 실패: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "네트워크 오류 발생: ${e.message}")
            null
        }
    }



}