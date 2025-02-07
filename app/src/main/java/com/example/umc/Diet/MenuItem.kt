package com.example.umc.Diet

data class MenuItem(
    val imageUrl: String,
    val name: String,
    val calories: String,
    val isSelected: Boolean = false,
    var isFavorite: Boolean = false,  // 즐겨찾기 상태 추가
    var isDietCompleted: Boolean = false  // 식단 완료 상태 추가
)
