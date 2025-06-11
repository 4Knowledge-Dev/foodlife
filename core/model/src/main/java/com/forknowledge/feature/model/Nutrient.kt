package com.forknowledge.feature.model

data class Nutrient(
    val name: String = "",
    val amount: Double = 0.0,
    val unit: String = "",
    val percentOfDailyNeeds: Double = 0.0
)
