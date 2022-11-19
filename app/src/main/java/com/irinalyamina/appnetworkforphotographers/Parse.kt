package com.irinalyamina.appnetworkforphotographers

import android.app.DatePickerDialog
import android.content.Context
import android.widget.ImageButton
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Parse {

    companion object {
        private const val formatDate = "dd-MM-yyyy"

        private val dateTimeFormatter = DateTimeFormatter.ofPattern(formatDate)

        fun stringToDate(date: String): LocalDate {
            return LocalDate.parse(date, dateTimeFormatter)
        }

        fun dateToString(date: LocalDate): String {
            return dateTimeFormatter.format(date)
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