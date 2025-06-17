package com.forknowledge.feature.planner

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

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
