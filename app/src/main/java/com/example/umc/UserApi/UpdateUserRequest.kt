package com.example.umc.UserApi


data class UpdateUserRequest(
    val email: String,
    val nickname: String,
    val birth : String,
    val name : String,
    val phoneNum : String
)
