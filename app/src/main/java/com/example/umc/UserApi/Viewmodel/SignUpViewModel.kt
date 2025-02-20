package com.example.umc.UserApi.Viewmodel

import androidx.lifecycle.ViewModel
import com.example.umc.UserApi.Request.SignUpRequest
import com.example.umc.UserApi.Request.OtpRequest
import com.example.umc.UserApi.Request.OtpValidationRequest
import com.example.umc.UserApi.Response.OtpResponse
import com.example.umc.UserApi.Response.OtpValidationResponse
import com.example.umc.UserApi.UserRepository

class SignUpViewModel : ViewModel() {
    // 기존 회원가입 관련 변수들
    var email: String? = null
    var password: String? = null
    var birth: String? = null
    var name: String? = null
    var phoneNum: String? = null
    var purpose: String? = null

    private val userRepository = UserRepository()

    // 기존 회원가입 요청 함수
    fun sendSignUpRequest(callback: (Boolean, String) -> Unit) {
        val request = SignUpRequest(
            email ?: "",
            password ?: "",
            birth ?: "",
            name ?: "",
            phoneNum ?: "",
            purpose ?: ""
        )
        userRepository.signUp(request, callback)
    }

    // OTP 요청 함수 추가
    suspend fun requestOtp(phoneNumber: String): Result<OtpResponse> {
        phoneNum = phoneNumber  // 전화번호를 ViewModel에 저장
        return userRepository.requestOtp(OtpRequest(phoneNumber).toString())
    }
    // OTP 인증 유효성 검사 함수 추가
    suspend fun verifyOtp(phoneNumber: String, code: String): Result<OtpValidationResponse> {
        return userRepository.validateOtp(phoneNumber, code) // OtpValidationRequest를 직접 넘기지 않음
    }


}