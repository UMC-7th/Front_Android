package com.example.umc.UserApi

data class SignUpRequest(
    val email: String,
    val password: String,
    val birth: String,
    val name: String,
    val phoneNum: String,
    val purpose: String
)