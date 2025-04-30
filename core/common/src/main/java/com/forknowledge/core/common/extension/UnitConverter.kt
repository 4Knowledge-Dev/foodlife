package com.forknowledge.core.common.extension

import java.util.Locale

const val CENTIMETERS_PER_FOOT = 30.48
const val POUNDS_PER_KILOGRAM = 2.2

/**
 * Convert centimeter to feet
 * @return value of feet
 */
fun Double.toFeet() = String.format(
    Locale.getDefault(),
    "%.1f",
    this / CENTIMETERS_PER_FOOT
).toDouble()

/**
 * Convert feet to centimeter
 * @return value of centimeter
 */
fun Double.toCm() = String.format(
    Locale.getDefault(),
    "%.1f",
    this * CENTIMETERS_PER_FOOT
).toDouble()

/**
 * Convert kilograms to pounds
 * @return value of pounds
 */
fun Double.toPounds() = String.format(
    Locale.getDefault(),
    "%.1f",
    this * POUNDS_PER_KILOGRAM
).toDouble()

/**
 * Convert pounds to kilograms
 * @return value of kilograms
 */
fun Double.toKilograms() = String.format(
    Locale.getDefault(),
    "%.1f",
    this / POUNDS_PER_KILOGRAM
).toDouble()

/**
 * Get integer and fraction part of float
 * @return Pair<Int, Int> of integer and fraction part
 */
fun Double.toIntegerAndFraction(): Pair<Int, Int> {
    val integer = this.toInt()
    val fraction = ((this - integer) * 10).toInt()
    return Pair(
        integer,
        fraction
    )
}

/**
 * Get a complete double value from integer and fraction part
 * @return double value
 */
fun Pair<Int, Int>.toCompleteDouble() = this.first + this.second / 10.0
