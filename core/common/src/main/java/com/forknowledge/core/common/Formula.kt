package com.forknowledge.core.common

import com.forknowledge.core.common.healthtype.Goal
import com.forknowledge.core.common.healthtype.NutrientType
import kotlin.math.roundToInt
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
    nutrient: NutrientType,
    amount: Float
): Float = amount * nutrient.kcal

/**
 * Calculate calories from nutrient amount.
 * @param nutrient nutrient type.
 * @param amount nutrient amount by gram.
 * @param totalCalories of the day.
 * @return nutrient ratio.
 */
fun nutrientAmountToCaloriesRatio(
    nutrient: NutrientType,
    amount: Float,
    totalCalories: Float
): Float = amount * nutrient.kcal / totalCalories

/**
 * Calculate nutrient amount(g) from calories.
 * @param nutrient nutrient type.
 * @param calories calories amount of the nutrient.
 * @return nutrient amount(g) from calories.
 */
fun caloriesToNutrientAmount(
    nutrient: NutrientType,
    calories: Double
): Int = (calories / nutrient.kcal).roundToInt()
