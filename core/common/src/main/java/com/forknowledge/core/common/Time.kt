package com.forknowledge.core.common

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
