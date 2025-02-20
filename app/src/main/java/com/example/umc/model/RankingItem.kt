package com.example.umc.model

import java.io.Serializable

data class RankingItem(
    val variety: String,
    val name: String,
    val rank: String,
    val imgUrl: String
) : Serializable
