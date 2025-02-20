package com.example.umc.model.response

import com.google.gson.annotations.SerializedName

data class KamisPriceResponse (
    @SerializedName("condition") val condition: String,
    @SerializedName("price") val price: String,
    @SerializedName("yyyy") val yyyy: String,
    @SerializedName("d40") val d40: String,
    @SerializedName("d30") val d30: String,
    @SerializedName("d20") val d20: String,
    @SerializedName("d10") val d10: String,
    @SerializedName("d0") val d0: String,
    @SerializedName("mx") val mx: String,
    @SerializedName("mn") val mn: String
)