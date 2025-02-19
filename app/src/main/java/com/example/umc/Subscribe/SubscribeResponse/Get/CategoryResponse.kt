package com.example.umc.Subscribe.SubscribeResponse.Get

data class CategoryResponse(
    val categoryName: String,
    val description: String
    // 필요한 다른 필드들 추가
)

enum class CategoryType {
    DAILY,      // 맛있는 일상 음식 구독
    DIET,       // 다이어트 식단 구독
    HEALTH      // 건강 음식 구독
}