package com.example.umc.UserApi

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val api = RetrofitClient.instance

    companion object {
        private const val PREF_NAME = "UserPreferences"
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"

        fun saveAuthToken(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
            Log.d("UserRepository", "토큰 저장됨: $token")
        }

        fun getAuthToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val token = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
            Log.d("UserRepository", "저장된 토큰 불러옴: $token")
            return token
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

    fun login(email: String, password: String): Call<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }

    suspend fun getUserProfile(context: Context): UserProfileResponse? {
        return try {
            Log.d("UserRepository", "프로필 조회 시작")

            val accessToken = getAuthToken(context)
            if (accessToken.isNullOrEmpty()) {
                Log.e("UserRepository", "액세스 토큰이 없거나 비어있습니다.")
                return null
            }

            Log.d("UserRepository", "API 호출 시작 - 사용 토큰: Bearer $accessToken")
            val response = api.getUserProfile("Bearer $accessToken")

            Log.d("UserRepository", "API 응답 수신 - 코드: ${response.code()}")
            Log.d("UserRepository", "API 응답 바디: ${response.body()}")

            when {
                response.isSuccessful && response.body() != null -> {
                    Log.d("UserRepository", "API 호출 성공: ${response.body()}")
                    response.body()
                }
                !response.isSuccessful -> {
                    Log.e("UserRepository", """
                       API 호출 실패
                       응답 코드: ${response.code()}
                       에러 메시지: ${response.errorBody()?.string()}
                       서버 메시지: ${response.message()}
                   """.trimIndent())
                    null
                }
                else -> {
                    Log.e("UserRepository", "응답은 성공했지만 바디가 null입니다")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getUserProfile 예외 발생", e)
            Log.e("UserRepository", "상세 에러: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}