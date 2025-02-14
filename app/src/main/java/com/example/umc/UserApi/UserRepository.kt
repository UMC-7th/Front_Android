package com.example.umc.UserApi

import android.content.Context
import android.util.Log
import com.example.umc.UserApi.APi.OtpApi
import com.example.umc.UserApi.Request.LoginRequest
import com.example.umc.UserApi.Request.OtpRequest
import com.example.umc.UserApi.Request.OtpValidationRequest
import com.example.umc.UserApi.Request.SignUpRequest
import com.example.umc.UserApi.Request.UpdateUserRequest
import com.example.umc.UserApi.Response.LoginResponse
import com.example.umc.UserApi.Response.OtpResponse
import com.example.umc.UserApi.Response.OtpValidationResponse
import com.example.umc.UserApi.Response.SignUpResponse
import com.example.umc.UserApi.Response.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val api = RetrofitClient.instance
    private val getUserApi = RetrofitClient.getApiService
    private val otpApi = RetrofitClient.otpApi

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
        val accessToken = getAuthToken(context)
        if (accessToken.isNullOrEmpty()) {
            Log.e("UserRepository", "액세스 토큰이 없습니다.")
            return null
        }

        Log.d("UserRepository", "현재 전달된 토큰: $accessToken")

        return try {
            Log.d("UserRepository", "요청 헤더: Authorization = Bearer $accessToken")
            val response = getUserApi.getUserProfile("Bearer $accessToken")
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                Log.e("UserRepository", "서버 응답 실패: ${response.code()}")
                Log.e("UserRepository", "에러 메시지: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "네트워크 오류 발생: ${e.message}")
            null
        }
    }

    suspend fun updateUserProfile(context: Context, updatedProfile: UpdateUserRequest): Boolean {
        val token = getAuthToken(context)
        if (token.isNullOrEmpty()) {
            Log.e("UserRepository", "액세스 토큰이 없습니다.")
            return false
        }

        return try {
            val response = RetrofitClient.updateUserApi.updateUserProfile("Bearer $token", updatedProfile)
            if (response.isSuccessful && response.body()?.user != null) {
                Log.d("UserRepository", "프로필 업데이트 성공: ${response.body()?.user}")
                true
            } else {
                Log.e("UserRepository", "프로필 업데이트 실패: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "네트워크 오류: ${e.message}")
            false
        }
    }

    suspend fun requestOtp(phoneNumber: String): Result<OtpResponse> {
        return try {
            Log.d("UserRepository", "OTP 요청 시작: $phoneNumber") // 요청 시작 로그

            val request = OtpRequest(phoneNumber)
            val response = otpApi.requestOtp(request)

            Log.d("UserRepository", "서버 응답 코드: ${response.code()}") // 응답 코드 로그

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: run {
                    Log.e("UserRepository", "응답이 비어 있습니다.") // 응답 바디 없음 로그
                    Result.failure(Exception("응답이 비어있습니다."))
                }
            } else {
                val errorMessage = response.errorBody()?.string()
                Log.e("UserRepository", "서버 오류: ${response.code()} - $errorMessage") // 서버 오류 로그
                Result.failure(Exception("서버 오류: ${response.code()} - $errorMessage"))
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "OTP 요청 실패: ${e.message}") // 예외 발생 로그
            Result.failure(Exception("네트워크 오류: ${e.message}"))
        }
    }


    suspend fun validateOtp(phoneNumber: String, code: String): Result<OtpValidationResponse> {
        return try {
            val request = OtpValidationRequest(phoneNumber, code)
            val response: OtpValidationResponse = RetrofitClient.otpValidationApi.validateOtp(request) // 반환 타입 확인
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }







}