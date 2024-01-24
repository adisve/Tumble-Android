package tumble.app.tumble.domain.models.util

import java.util.Calendar
import java.util.Date

data class DateComponents(
    val year: Int? = null,
    val month: Int? = null,
    val day: Int? = null,
    val hour: Int? = null,
    val minute: Int? = null,
    val second: Int? = null
) {
    fun toDate(): Date? {
        val calendar = Calendar.getInstance()
        if (year != null) calendar[Calendar.YEAR] = year
        if (month != null) calendar[Calendar.MONTH] = month - 1 // Calendar.MONTH is 0-based in Java/Kotlin
        if (day != null) calendar[Calendar.DAY_OF_MONTH] = day
        if (hour != null) calendar[Calendar.HOUR_OF_DAY] = hour
        if (minute != null) calendar[Calendar.MINUTE] = minute
        if (second != null) calendar[Calendar.SECOND] = second
        return if (listOfNotNull(year, month, day, hour, minute, second).isEmpty()) null else calendar.time
    }

    companion object {
        fun fromDate(date: Date): DateComponents {
            val calendar = Calendar.getInstance()
            calendar.time = date
            return DateComponents(
                year = calendar[Calendar.YEAR],
                month = calendar[Calendar.MONTH] + 1,
                day = calendar[Calendar.DAY_OF_MONTH],
                hour = calendar[Calendar.HOUR_OF_DAY],
                minute = calendar[Calendar.MINUTE],
                second = calendar[Calendar.SECOND]
            )
        }
    }
}