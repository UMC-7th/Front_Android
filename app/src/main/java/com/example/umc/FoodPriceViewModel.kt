package com.example.umc

import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FoodPriceViewModel : ViewModel() {

    data class MidQuality(val name: String, val price: Int, val rate: Double)
    data class Price(val dateTime: Long, val price: Float)

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val prices = listOf(
        Price(LocalDate.parse("2024-12-29", formatter).toEpochDay(), 285f),
        Price(LocalDate.parse("2024-12-30", formatter).toEpochDay(), 285f),
        Price(LocalDate.parse("2024-12-31", formatter).toEpochDay(), 278f),
        Price(LocalDate.parse("2025-01-01", formatter).toEpochDay(), 276f),
        Price(LocalDate.parse("2025-01-02", formatter).toEpochDay(), 277f),
        Price(LocalDate.parse("2025-01-03", formatter).toEpochDay(), 278f),
        Price(LocalDate.parse("2025-01-04", formatter).toEpochDay(), 282f)
    )

    val midQuality = listOf(
        MidQuality("중품", 150, -8.09)
    )
}
