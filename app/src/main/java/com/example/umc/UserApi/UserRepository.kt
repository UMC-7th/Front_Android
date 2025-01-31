package com.example.umc.UserApi

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val api = RetrofitClient.instance

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
    //로그인 처리
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback(false, "네트워크 오류: ${t.message}")
            }
        })
    }

    // 로그인 요청을 Call<LoginResponse>로 반환 (callback 제거)
    fun login(email: String, password: String): Call<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }
}
