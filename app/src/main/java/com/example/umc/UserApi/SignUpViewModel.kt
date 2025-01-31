package com.example.umc.UserApi

import androidx.lifecycle.ViewModel

//requestbody+viewmodel을 통해 일괄관리
class SignUpViewModel : ViewModel() {
    var email: String? = null
    var password: String? = null
    var birth: String? = null
    var name: String? = null
    var phoneNum: String? = null
    var purpose: String? = null

    private val userRepository = UserRepository()

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
}
