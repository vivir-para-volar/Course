package com.irinalyamina.appnetworkforphotographers

import android.app.DatePickerDialog
import android.content.Context
import android.widget.ImageButton
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Parse {

    companion object {
        private const val formatDateTime = "dd.MM.yyyy HH:mm"
        private const val formatDate = "dd.MM.yyyy"

        private val dateTimeFormatter = DateTimeFormatter.ofPattern(formatDateTime)
        private val dateFormatter = DateTimeFormatter.ofPattern(formatDate)

        var zoneId: ZoneId = ZoneId.systemDefault()


        fun stringToDate(date: String): LocalDate {
            return LocalDate.parse(date, dateFormatter)
        }

        fun dateToString(date: LocalDate): String {
            return dateFormatter.format(date)
        }

        fun stringToDateTime(dateTime: String): LocalDateTime {
            return LocalDateTime.parse(dateTime, dateTimeFormatter)
        }

        fun dateTimeToString(dateTime: LocalDateTime): String {
            return dateTimeFormatter.format(dateTime)
        }

        fun dateToUnixTime(date: LocalDate): Long {
            return date.atStartOfDay(zoneId).toEpochSecond()
        }

        fun unixTimeToDate(date: Long): LocalDate {
            return Instant.ofEpochSecond(date).atZone(zoneId).toLocalDate()
        }

        fun dateTimeToUnixTime(dateTime: LocalDateTime): Long {
            return dateTime.atZone(zoneId).toEpochSecond()
        }

        fun unixTimeToDateTime(dateTime: Long): LocalDateTime {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(dateTime), zoneId)
        }


        fun onDatePicker(context: Context, textView: TextView, button: ImageButton) {
            textView.text = SimpleDateFormat(formatDate).format(System.currentTimeMillis())

            val cal = Calendar.getInstance()

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val sdf = SimpleDateFormat(formatDate, Locale.US)
                    textView.text = sdf.format(cal.time)
                }

            button.setOnClickListener {
                DatePickerDialog(
                    context,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }
}