package com.forknowledge.feature.model.userdata

import java.util.Date

data class User(
    val username: String = "",
    val hashKey: String = "",
    @JvmField
    val isNewUser: Boolean = false,
    val birthday: Date? = null,
    val gender: Boolean = true,
    val height: Double = 0.0,
    val goal: Long = 0,
    val currentWeight: Double = 0.0,
    val targetWeight: Double = 0.0,
    val weighPerWeek: Double = 0.0,
    @JvmField
    val isHeightUnitCm: Boolean = true,
    @JvmField
    val isWeightUnitKg: Boolean = true,
    val activityLevel: Long = 0,
    val diet: String = "",
    val excludes: List<String> = emptyList(),
    val targetNutrition: TargetNutrition = TargetNutrition()
)
