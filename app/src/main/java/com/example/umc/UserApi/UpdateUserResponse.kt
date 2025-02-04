package com.example.umc.UserApi

data class UpdateUserResponse(
    val user: UserUpdateData  // `user` 필드를 포함하여 Swagger 구조와 일치시킴
)

data class UserUpdateData(
    val nickname: String,
    val email: String,
    val birth: String,
    val name: String,
    val phoneNum: String
)