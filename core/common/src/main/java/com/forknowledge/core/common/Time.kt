package com.forknowledge.core.common

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

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
