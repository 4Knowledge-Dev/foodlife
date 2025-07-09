package com.forknowledge.feature.model

data class Nutrient(
    val name: String = "",
    val amount: Float = 0f,
    val unit: String = "",
    val dailyPercentValue: Int = 0
)
