package com.obss.firstapp.utils.ext.string

import com.obss.firstapp.utils.ext.formatAndCalculateAge
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FormatAndCalculateAgeKtTest {
    @Test
    fun `formatAndCalculateAge formats date and calculates age correctly`() {
        val dateStr = "2000-01-01"
        val expectedDateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)?.let {
                SimpleDateFormat(
                    "MMMM dd, yyyy",
                    Locale.ENGLISH,
                ).format(it)
            }
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val age = currentYear - 2000
        val expected = "$expectedDateFormat ($age)"
        val actual = dateStr.formatAndCalculateAge()
        assertEquals(expected, actual)
    }

    @Test
    fun `formatAndCalculateAge returns empty string for invalid date format`() {
        val dateStr = "invalid_date"
        val expected = ""
        val actual = dateStr.formatAndCalculateAge()
        assertEquals(expected, actual)
    }

    @Test
    fun `formatAndCalculateAge calculates age correctly for today`() {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        val expectedDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
        val expected = "$expectedDateFormat (0)"
        val actual = todayDate.formatAndCalculateAge()
        assertEquals(expected, actual)
    }

    @Test
    fun `formatAndCalculateAge calculates age correctly for a different date`() {
        val dateStr = "1995-05-15"
        val expectedDateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)?.let {
                SimpleDateFormat(
                    "MMMM dd, yyyy",
                    Locale.ENGLISH,
                ).format(it)
            }
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val birthYear = 1995
        val age = currentYear - birthYear
        val expected = "$expectedDateFormat ($age)"
        val actual = dateStr.formatAndCalculateAge()
        assertEquals(expected, actual)
    }
}
