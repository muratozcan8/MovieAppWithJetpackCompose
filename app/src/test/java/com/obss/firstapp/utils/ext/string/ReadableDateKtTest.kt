package com.obss.firstapp.utils.ext.string

import com.obss.firstapp.utils.ext.formatToReadableDate
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.util.TimeZone

class ReadableDateKtTest {
    @Test
    fun `formatToReadableDate formats valid date correctly`() {
        val dateStr = "2024-08-21T15:45:00.000Z"
        val expected = "August 21, 2024"
        val actual = dateStr.formatToReadableDate()
        assertEquals(expected, actual)
    }

    @Test
    fun `formatToReadableDate formats another valid date correctly`() {
        val dateStr = "2023-12-31T23:59:59.999Z"
        val expected = "December 31, 2023"
        val actual = dateStr.formatToReadableDate()
        assertEquals(expected, actual)
    }

    @Test
    fun `formatToReadableDate returns empty string for invalid date format`() {
        val dateStr = "invalid_date_format"
        val expected = ""
        val actual = dateStr.formatToReadableDate()
        assertEquals(expected, actual)
    }

    @Test
    fun `formatToReadableDate handles invalid date format gracefully`() {
        val dateStr = "2024/08/21 15:45:00"
        val expected = ""
        val actual = dateStr.formatToReadableDate()
        assertEquals(expected, actual)
    }

    @Test
    fun `formatToReadableDate handles null case`() {
        val dateStr: String? = null
        val actual = dateStr?.formatToReadableDate()
        assertNull(actual)
    }

    @Test
    fun `formatToReadableDate handles different time zones`() {
        val originalTimeZone = TimeZone.getDefault()
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("PST"))
            val dateStr = "2024-08-21T23:45:00.000Z"
            val expected = "August 21, 2024"
            val actual = dateStr.formatToReadableDate()
            assertEquals(expected, actual)
        } finally {
            TimeZone.setDefault(originalTimeZone)
        }
    }

    @Test
    fun `formatToReadableDate formats the first day of the year correctly`() {
        val dateStr = "2024-01-01T00:00:00.000Z"
        val result = dateStr.formatToReadableDate()
        assertEquals("January 01, 2024", result)
    }
}
