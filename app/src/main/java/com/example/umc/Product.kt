package com.example.umc

// 상품 데이터 클래스
data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val unit: String,
    val imageUrl: String
)