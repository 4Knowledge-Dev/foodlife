package com.forknowledge.core.common.extension

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.convertMillisToDate(): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Long.toAge(): Int {
    val currentCalendar = Calendar.getInstance()
    val birthCalendar = Calendar.getInstance()
    birthCalendar.time = Date(this)

    var age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

    if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
        age--
    }

    return age
}

fun Date.startOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    return calendar.time
}

fun Date.endOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 24)
    return calendar.time
}

fun Date.toDayMonthDateString(): String {
    val formatter = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
    return formatter.format(this)
}

fun Date.nextDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}

fun Date.previousDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    return calendar.time
}

/**
 * Get firestore document id by getting date in millis.
 * @return firestore document id should be used when create a new nutrition track date.
 */
fun Date.toFirestoreDocumentIdByDate(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis.toString()
}

/**
 * Convert time in milliseconds to Date for working with firestore.
 * @return [Date] object.
 */
fun Long.toFirestoreDateTime(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    calendar.set(Calendar.HOUR_OF_DAY, 12)
    return calendar.time
}

/**
 * Get local date from Epoch time.
 * @return [LocalDate] object.
 */
fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochSecond(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun LocalDate.toEpochSeconds(): Long {
    return this.atStartOfDay(ZoneId.of("GMT")).toEpochSecond()
}

fun LocalDate.toDayAndDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd EEE", Locale.getDefault())
    return formatter.format(this)
}

fun LocalDate.toYearMonthDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(this)
}
