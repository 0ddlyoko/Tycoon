package me.oddlyoko.tycoon.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestCustomBigNumber {

    @Test
    fun testZero() {
        var number = CustomBigNumber(0, 0).verify()
        assertEquals(0, number.number)
        assertEquals(1, number.multiplicator)

        number = CustomBigNumber(1_000_000, 2).verify()
        number.remove(CustomBigNumber(1_000_000, 2))

        assertEquals(0, number.number)
        assertEquals(1, number.multiplicator)
    }

    @Test
    fun testSameNumber() {
        var number = CustomBigNumber(1_000, 1).verify()
        var number2 = CustomBigNumber(1_000, 1).verify()
        assertEquals(number.number, number2.number)
        assertEquals(number.multiplicator, number2.multiplicator)

        number = CustomBigNumber(1_000_000, 1).verify()
        number2 = CustomBigNumber(1_000, 2).verify()
        assertEquals(number.number, number2.number)
        assertEquals(number.multiplicator, number2.multiplicator)
        assertEquals(1_000_000, number.number)
        assertEquals(1, number.multiplicator)

        number = CustomBigNumber(1_000_000_000, 1).verify()
        number2 = CustomBigNumber(1_000_000, 2).verify()
        val number3 = CustomBigNumber(1_000, 3).verify()
        assertEquals(number.number, number2.number)
        assertEquals(number.multiplicator, number2.multiplicator)
        assertEquals(number.number, number3.number)
        assertEquals(number.multiplicator, number3.multiplicator)
        assertEquals(1_000_000, number.number)
        assertEquals(2, number.multiplicator)
    }

    @Test
    fun testAddCustomNumber() {
        val number = CustomBigNumber(1_000, 1)

        number.add(CustomBigNumber(1_000, 1))
        assertEquals(2_000, number.number)
        assertEquals(1, number.multiplicator)

        number.add(CustomBigNumber(1_000, 2))
        assertEquals(1_002_000, number.number)
        assertEquals(1, number.multiplicator)

        number.add(CustomBigNumber(1_000, 2))
        assertEquals(2_002_000, number.number)
        assertEquals(1, number.multiplicator)

        number.add(CustomBigNumber(100_000, 2))
        assertEquals(102_002_000, number.number)
        assertEquals(1, number.multiplicator)

        number.add(CustomBigNumber(100_000, 5))
        assertEquals(100_000_000, number.number)
        assertEquals(4, number.multiplicator)

        number.add(CustomBigNumber(100_000, 2))
        assertEquals(100_000_000, number.number)
        assertEquals(4, number.multiplicator)

        number.add(CustomBigNumber(100_000, 3))
        assertEquals(100_000_100, number.number)
        assertEquals(4, number.multiplicator)

        number.add(CustomBigNumber(100_000, 5))
        assertEquals(200_000_100, number.number)
        assertEquals(4, number.multiplicator)

        number.add(CustomBigNumber(1_000_000, 5))
        assertEquals(1_200_000, number.number)
        assertEquals(5, number.multiplicator)
    }

    @Test
    fun testRemoveCustomNumber() {
        var number = CustomBigNumber(1_000, 1)
        number.remove(CustomBigNumber(1_000, 1))
        assertEquals(0, number.number)
        assertEquals(1, number.multiplicator)

        number.add(CustomBigNumber(1_000, 2))
        assertEquals(1_000_000, number.number)
        assertEquals(1, number.multiplicator)

        number.remove(CustomBigNumber(1_000, 1))
        assertEquals(999_000, number.number)
        assertEquals(1, number.multiplicator)

        number.add(CustomBigNumber(1_000_000, 2))
        assertEquals(1_000_999, number.number)
        assertEquals(2, number.multiplicator)

        number.remove(CustomBigNumber(1_000_000, 1))
        assertEquals(999_999_000, number.number)
        assertEquals(1, number.multiplicator)

        number = CustomBigNumber(1_000_000, 10)
        number.remove(CustomBigNumber(1_000_000, 7))
        assertEquals(1_000_000, number.number)
        assertEquals(10, number.multiplicator)

        number.remove(CustomBigNumber(1_000_000, 8))
        assertEquals(999_999_000, number.number)
        assertEquals(9, number.multiplicator)

        number.remove(CustomBigNumber(1, 40))
        assertEquals(0, number.number)
        assertEquals(1, number.multiplicator)

        // Check remove should be 0
        number = CustomBigNumber(100, 1)
        number.remove(CustomBigNumber(101, 1))
        assertEquals(0, number.number)
        assertEquals(1, number.multiplicator)
    }

    @Test
    fun testToString() {
        assertEquals("0", CustomBigNumber(0, 1).toString())
        assertEquals("1", CustomBigNumber(1, 1).toString())
        assertEquals("1K", CustomBigNumber(1_000, 1).toString())
        assertEquals("1M", CustomBigNumber(1_000_000, 1).toString())
        assertEquals("1M", CustomBigNumber(1_000, 2).toString())
        assertEquals("1M475K", CustomBigNumber(1_475_876, 1).toString())
        assertEquals("1M", CustomBigNumber(1_000_876, 1).toString())
        assertEquals("475K876", CustomBigNumber(475_876, 1).toString())

        assertEquals("1B", CustomBigNumber(1_000_000_000, 1).toString())
        assertEquals("1B", CustomBigNumber(1_000_000, 2).toString())
        assertEquals("1B", CustomBigNumber(1_000, 3).toString())
        assertEquals("1B", CustomBigNumber(1, 4).toString())

        assertEquals("1a", CustomBigNumber(1_000_000, 4).toString())
        assertEquals("1aa", CustomBigNumber(1_000_000, 30).toString())
        assertEquals("1aa975z", CustomBigNumber(1_975_000, 30).toString())

        assertEquals("1cz", CustomBigNumber(1_000_000, 107).toString())
        assertEquals("∞", CustomBigNumber(1_000_000, 108).toString())
    }

    @Test
    fun testFromString() {
        assertEquals(CustomBigNumber(0, 1), CustomBigNumber.fromString("0"))
        assertEquals(CustomBigNumber(1_000, 1), CustomBigNumber.fromString("1K"))
        assertEquals(CustomBigNumber(1_000_000, 1), CustomBigNumber.fromString("1M"))
        assertEquals(CustomBigNumber(1_234_000, 1), CustomBigNumber.fromString("1M234K"))
        assertEquals(CustomBigNumber(1_234_567, 1), CustomBigNumber.fromString("1M234K567"))
        assertEquals(CustomBigNumber(1_234_567, 2), CustomBigNumber.fromString("1B234M567K890"))


        assertEquals(CustomBigNumber(0, 1), CustomBigNumber.fromString("0"))
        assertEquals(CustomBigNumber(1, 1), CustomBigNumber.fromString("1"))
        assertEquals(CustomBigNumber(1_000, 1), CustomBigNumber.fromString("1K"))
        assertEquals(CustomBigNumber(1_000_000, 1), CustomBigNumber.fromString("1M"))
        assertEquals(CustomBigNumber(1_000, 2), CustomBigNumber.fromString("1M"))
        assertEquals(CustomBigNumber(1_475_000, 1), CustomBigNumber.fromString("1M475K"))
        assertEquals(CustomBigNumber(1_000_000, 1), CustomBigNumber.fromString("1M"))
        assertEquals(CustomBigNumber(475_876, 1), CustomBigNumber.fromString("475K876"))

        assertEquals(CustomBigNumber(1_000_000_000, 1), CustomBigNumber.fromString("1B"))
        assertEquals(CustomBigNumber(1_000_000, 2), CustomBigNumber.fromString("1B"))
        assertEquals(CustomBigNumber(1_000, 3), CustomBigNumber.fromString("1B"))
        assertEquals(CustomBigNumber(1, 4), CustomBigNumber.fromString("1B"))

        assertEquals(CustomBigNumber(1_000_000, 4), CustomBigNumber.fromString("1a"))
        assertEquals(CustomBigNumber(1_000_000, 30), CustomBigNumber.fromString("1aa"))
        assertEquals(CustomBigNumber(1_975_000, 30), CustomBigNumber.fromString("1aa975z"))

        assertEquals(CustomBigNumber(1_000_000, 107), CustomBigNumber.fromString("1cz"))
        assertEquals(CustomBigNumber(1_000_000, 108), CustomBigNumber.fromString("∞"))
    }
}
