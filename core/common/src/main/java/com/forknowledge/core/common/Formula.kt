package com.forknowledge.core.common

import com.forknowledge.core.common.healthtype.Goal
import com.forknowledge.core.common.healthtype.NutrientType
import kotlin.math.roundToInt

/**
 * Calculate TDEE(Total Daily Energy Expenditure).
 * @param gender user gender.
 * @param weight user weight.
 * @param height user height.
 * @param age user age.
 * @param activityIndex user activity index.
 * @return TDEE.
 */
fun calculateTDEE(
    gender: Boolean,
    weight: Double,
    height: Double,
    age: Int,
    activityIndex: Float,
): Double {
    val bmr = 10 * weight + 6.25 * height - 5 * age + if (gender) 5 else -161
    return bmr * activityIndex
}

/**
 * Calculate target calories per day to gain or loss [weightPerWeek].
 * @param gender user gender.
 * @param weight user weight.
 * @param height user height.
 * @param age user age.
 * @param activityIndex user activity index.
 * @param goal user goal.
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
    val tdee = calculateTDEE(gender, weight, height, age, activityIndex)
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
 * @param amount nutrient amount(g).
 * @return calories from nutrient amount.
 */
fun nutrientAmountToCalories(
    nutrient: NutrientType,
    amount: Float
): Float = amount * nutrient.kcal

/**
 * Calculate calories from nutrient amount(g).
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
 * Calculate nutrient amount(g) from it's ratio.
 * @param nutrient nutrient type.
 * @param totalCalories total calories of 3 nutrients carbs, protein, fat.
 * @param ratio nutrient ratio.
 * @return nutrient amount(g) from calories.
 */
fun nutrientRatioToNutrientAmount(
    nutrient: NutrientType,
    totalCalories: Int,
    ratio: Double
): Int = (totalCalories * ratio / nutrient.kcal).roundToInt()
