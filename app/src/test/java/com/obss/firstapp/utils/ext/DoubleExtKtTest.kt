package com.obss.firstapp.utils.ext

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class DoubleExtKtTest {
    @Test
    fun `roundToSingleDecimal rounds up correctly`() {
        val value = 3.456
        val expected = "3.5"
        val actual = value.roundToSingleDecimal()
        assertEquals(expected, actual)
    }

    @Test
    fun `roundToSingleDecimal rounds down correctly`() {
        val value = 2.443
        val expected = "2.4"
        val actual = value.roundToSingleDecimal()
        assertEquals(expected, actual)
    }

    @Test
    fun `test roundToSingleDecimal`() {
        val doubleValue = 3.14159
        val expected = "3.1"
        val actual = doubleValue.roundToSingleDecimal()
        assertEquals(expected, actual)
    }

    @Test
    fun `roundToSingleDecimal handles already rounded number`() {
        val value = 7.0
        val expected = "7.0"
        val actual = value.roundToSingleDecimal()
        assertEquals(expected, actual)
    }

    @Test
    fun `roundToSingleDecimal handles one decimal number`() {
        val value = 5.2
        val expected = "5.2"
        val actual = value.roundToSingleDecimal()
        assertEquals(expected, actual)
    }

    @Test
    fun `test roundToSingleDecimal with zero value`() {
        val doubleValue = 0.0
        val expected = "0.0"
        val actual = doubleValue.roundToSingleDecimal()
        assertEquals(expected, actual)
    }

    @Test
    fun `roundToSingleDecimal handles negative number rounding up`() {
        val value = -1.666
        val expected = "-1.7"
        val actual = value.roundToSingleDecimal()
        assertEquals(expected, actual)
    }

    @Test
    fun `roundToSingleDecimal handles negative number rounding down`() {
        val value = -1.222
        val expected = "-1.2"
        val actual = value.roundToSingleDecimal()
        assertEquals(expected, actual)
    }
}
