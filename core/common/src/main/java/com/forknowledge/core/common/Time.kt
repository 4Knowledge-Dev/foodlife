package com.forknowledge.core.common

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

const val DAYS_OF_THE_WEEK = 7

/**
 * Get current date to execute with firestore.
 * @return [Date] object.
 */
fun getCurrentDateTime(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(Calendar.HOUR_OF_DAY, 12)
    return calendar.time
}

fun getLastThirtyDays(): Pair<Date, Date> {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val endDate = calendar.time
    calendar.add(Calendar.DAY_OF_YEAR, -30)
    val startDate = calendar.time

    return Pair(startDate, endDate)
}

fun getCurrentDate(): LocalDate {
    return LocalDate.now()
}

fun getFirstDayOfWeek(): LocalDate {
    val today = LocalDate.now()
    return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
}

fun getCurrentWeekDays(): List<LocalDate> {
    val firstDayOfWeek = getFirstDayOfWeek()
    val weekDays = mutableListOf<LocalDate>()
    for (i in 0..6) {
        weekDays.add(firstDayOfWeek.plusDays(i.toLong()))
    }
    return weekDays
}

fun getGreetingText(): String {
    val calendar = Calendar.getInstance()
    return when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 4..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..22 -> "Good evening"
        else -> "Time to sleep!"
    }
}

/**
 * Get end date to complete progress diet.
 * @param [startDate] start date of progress.
 * @param [weightPerWeek] weight loss/gain per week.
 * @param [weightDifference] weight need to loss/gain.
 * @return [Date] end date of progress.
 */
fun getEndDateProgress(
    startDate: Date,
    weightPerWeek: Double,
    weightDifference: Double,
): Date {
    val calendar = Calendar.getInstance()
    calendar.time = startDate
    val daysToEndDate = (weightDifference / weightPerWeek * DAYS_OF_THE_WEEK).roundToInt()
    calendar.add(Calendar.DAY_OF_YEAR, daysToEndDate)
    return calendar.time
}

fun getCheckInProgressDate(
    startDate: Date
): Date {
    val calendar = Calendar.getInstance()
    calendar.time = startDate
    calendar.add(Calendar.DAY_OF_YEAR, 5)
    return calendar.time
}