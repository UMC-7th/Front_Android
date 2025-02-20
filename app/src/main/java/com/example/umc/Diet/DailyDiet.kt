package com.example.umc.Diet

data class DietItem(
    val date: String = "",    // "01.01" 형식
    val day: String = "",    // "수" 형식
    val breakfast: String = "", // 아침 메뉴
    val lunch: String = "",    // 점심 메뉴
    val dinner: String = ""    // 저녁 메뉴
)