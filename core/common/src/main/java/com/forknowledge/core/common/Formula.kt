package com.forknowledge.core.common

import com.forknowledge.core.common.healthtype.Goal
import com.forknowledge.core.common.healthtype.Nutrient
import kotlin.math.roundToLong

/**
 * Calculate target calories per day to gain or loss [weightPerWeek].
 * @param weightPerWeek weight loss/gain per week.
 * @return target calories per day.
 */
fun calculateTargetCalories(
    gender: Boolean,
    weight: Double,
    height: Double,
    age: Int,
    activityIndex: Float,
    goal: Goal,
    weightPerWeek: Double
): Int {
    val bmr = 10 * weight + 6.25 * height - 5 * age + if (gender) 5 else -161
    val tdee = bmr * activityIndex
    return when (goal) {
        Goal.LOSE_WEIGHT -> (tdee - getDeficitOrExcessCaloriesPerDay(weightPerWeek)).toInt()
        Goal.EAT_HEALTHY -> tdee.toInt()
        else -> (tdee + getDeficitOrExcessCaloriesPerDay(weightPerWeek)).toInt()
    }
}

/**
 * Calculate calories need deficiting/excessing per day to loss/gain [weightPerWeek].
 * Instant 100 is calories equivalent to 0,1 kg of fat.
 * @param weightPerWeek weight loss/gain per week.
 * @return calories need deficiting or excessing per day loss/gain [weightPerWeek].
 */
fun getDeficitOrExcessCaloriesPerDay(weightPerWeek: Double) = weightPerWeek / 0.1 * 100

/**
 * Calculate calories from nutrient amount.
 * @param nutrient nutrient type.
 * @param amount nutrient amount.
 * @return calories from nutrient amount.
 */
fun nutrientAmountToCalories(
    nutrient: Nutrient,
    amount: Long
): Long = amount * nutrient.kcal

/**
 * Calculate nutrient amount(g) from calories.
 * @param nutrient nutrient type.
 * @param calories calories.
 * @return nutrient amount(g) from calories.
 */
fun caloriesToNutrientAmount(
    nutrient: Nutrient,
    calories: Double
): Long = (calories / nutrient.kcal).roundToLong()
