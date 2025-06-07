package com.forknowledge.core.common

import java.util.Calendar
import java.util.Date

fun getCurrentDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(Calendar.HOUR_OF_DAY, 12)
    return calendar.time
}
