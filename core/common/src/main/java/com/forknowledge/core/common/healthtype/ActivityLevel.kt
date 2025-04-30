package com.forknowledge.core.common.healthtype


enum class ActivityLevel(
    val level: String,
    val description: String,
    val index: Float
) {
    SEDENTARY("Sedentary", "Office worker, little or no exercise", 1.2F),
    LIGHT("Lightly active", "Walk gently, do housework, light exercise 1-3 days/week", 1.375F),
    MODERATE("Moderately active", "Fast walking, cycling, exercise 3-5 days/week", 1.55F),
    ACTIVE("Very active", "Jogging, swimming, exercise 6-7 days/week", 1.725F),
    EXTRA("Extra active", "Daily high-intensity exercise, professional athlete", 1.9F)
}

val activityLevels = ActivityLevel.entries.toList()