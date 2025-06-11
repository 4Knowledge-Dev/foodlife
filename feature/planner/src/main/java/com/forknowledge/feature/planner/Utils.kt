package com.forknowledge.feature.planner

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun getCurrentDate(): LocalDate {
    return LocalDate.now()
}

fun getCurrentWeekDays(): List<LocalDate> {
    val today = LocalDate.now()
    val firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    val weekDays = mutableListOf<LocalDate>()
    for (i in 0..6) {
        weekDays.add(firstDayOfWeek.plusDays(i.toLong()))
    }
    return weekDays
}
