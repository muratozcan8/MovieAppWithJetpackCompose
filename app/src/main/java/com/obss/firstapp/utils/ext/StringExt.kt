package com.obss.firstapp.utils.ext

import android.text.Spanned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.core.text.HtmlCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.formatToReadableDate(): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormatter.timeZone = TimeZone.getTimeZone("UTC")

    val outputFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
    outputFormatter.timeZone = TimeZone.getTimeZone("UTC") // Set output time zone to UTC

    return try {
        val birthDate = inputFormatter.parse(this)
        val formattedDate = birthDate?.let { outputFormatter.format(it) }
        formattedDate ?: ""
    } catch (e: ParseException) {
        ""
    }
}

fun String.formatAndCalculateAge(): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        val birthDate = inputFormatter.parse(this)

        val outputFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        val formattedDate = birthDate?.let { outputFormatter.format(it) } ?: ""

        val birthCalendar = Calendar.getInstance().apply { time = birthDate ?: Date() }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        "$formattedDate ($age)"
    } catch (e: Exception) {
        ""
    }
}

fun String.toAnnotatedString(): AnnotatedString {
    val spanned: Spanned = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)

    return buildAnnotatedString {
        append(spanned.toString())
    }
}
