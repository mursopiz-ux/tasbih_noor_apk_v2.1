package com.example.util

import android.icu.util.IslamicCalendar
import android.icu.util.ULocale
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val hijriMonths = listOf(
        "Muharram",
        "Safar",
        "Rabiul Awal",
        "Rabiul Akhir",
        "Jumadil Awal",
        "Jumadil Akhir",
        "Rajab",
        "Sya'ban",
        "Ramadhan",
        "Syawal",
        "Dzulqa'dah",
        "Dzulhijjah"
    )

    fun todayDateKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    fun timestampToDateKey(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date(timestamp))
    }

    fun getStartOfTodayMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun getStartOfWeekMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, -6)
        }.timeInMillis
    }

    fun getStartOfMonthMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun getFormattedGregorianDate(): String {
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date())
    }

    fun getFormattedHijriDate(): String {
        return try {
            val islamicCal = IslamicCalendar(ULocale("@calendar=islamic-umalqura"))
            val day = islamicCal.get(IslamicCalendar.DAY_OF_MONTH)
            val monthIndex = islamicCal.get(IslamicCalendar.MONTH).coerceIn(0, 11)
            val year = islamicCal.get(IslamicCalendar.YEAR)
            "$day ${hijriMonths[monthIndex]} $year H"
        } catch (_: Exception) {
            "Kalender Hijriah"
        }
    }

    fun getLastNDaysKeys(days: Int): List<Pair<String, String>> {
        val keyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val labelFormat = SimpleDateFormat("EEE", Locale("id", "ID"))
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -(days - 1))
        val result = mutableListOf<Pair<String, String>>()
        for (i in 0 until days) {
            val date = cal.time
            result.add(keyFormat.format(date) to labelFormat.format(date))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return result
    }
}
