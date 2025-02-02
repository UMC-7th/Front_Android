package com.example.umc.model.request

data class PostCompleteMealRequest(
    val userId: Int,
    val mealId: Int,
    val mealDate: String
)