package com.forknowledge.feature.model

import java.util.Date

data class User(
    val hashKey: String = "",
    val isNewUser: Boolean = true,
    val birthday: Date? = null,
    val gender: Boolean = true,
    val height: Long = 0,
    val currentWeight: Long = 0,
    val targetWeight: Long = 0,
    val activityLevel: Long = 0,
    val diet: Long = 0,
    val excludes: List<String> = emptyList(),
    val targetNutrition: TargetNutrition = TargetNutrition()
)
