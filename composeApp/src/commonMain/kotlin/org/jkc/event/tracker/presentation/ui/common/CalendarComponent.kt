package org.jkc.event.tracker.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import kotlinx.datetime.*

@Composable
fun CalendarComponent(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var currentMonth by remember {
        mutableStateOf(YearMonth(selectedDate.year, selectedDate.month))
    }

    val daysInMonth = currentMonth.lengthOfMonth()
    val monthName = currentMonth.month.name.lowercase()
        .replaceFirstChar { it.uppercase() } // Capitalizar

    Column(modifier = Modifier.padding(16.dp)) {
        // Encabezado
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Mes anterior")
            }

            Text(
                text = "$monthName ${currentMonth.year}",
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Mes siguiente")
            }
        }

        Spacer(Modifier.height(8.dp))

        // Días
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(300.dp)
        ) {
            items((1..daysInMonth).toList()) { day ->
                val date = LocalDate(currentMonth.year, currentMonth.month, day)
                val isSelected = date == selectedDate
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

// Año-Mes con funciones auxiliares
data class YearMonth(val year: Int, val month: Month) {
    fun lengthOfMonth(): Int {
        val firstDay = LocalDate(year, month, 1)
        return firstDay.monthLength()
    }

    fun plusMonths(months: Int): YearMonth {
        val totalMonths = (year * 12 + month.number - 1) + months
        val newYear = totalMonths / 12
        val newMonth = Month((totalMonths % 12) + 1)
        return YearMonth(newYear, newMonth)
    }

    fun minusMonths(months: Int) = plusMonths(-months)
}

fun LocalDate.monthLength(): Int {
    val nextMonth = this.monthNumber % 12 + 1
    val yearAdjust = if (this.monthNumber == 12) this.year + 1 else this.year
    val firstOfNext = LocalDate(yearAdjust, Month(nextMonth), 1)
    return firstOfNext.minus(DatePeriod(days = 1)).dayOfMonth
}