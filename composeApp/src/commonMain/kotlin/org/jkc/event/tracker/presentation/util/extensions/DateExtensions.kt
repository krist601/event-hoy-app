package org.jkc.event.tracker.presentation.util.extensions

import kotlinx.datetime.LocalDate


fun LocalDate.simpleDateFormat(): String {
    val day = this.dayOfMonth.toString().padStart(2, '0')
    val month = this.monthNumber.toString().padStart(2, '0')
    val year = this.year.toString()
    return "$day-$month-$year"
}