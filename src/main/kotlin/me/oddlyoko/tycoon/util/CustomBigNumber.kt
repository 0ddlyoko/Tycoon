package me.oddlyoko.tycoon.util

class CustomBigNumber(
    var number: Int,
    var multiplicator: Int,
) {

    fun set(number: Int, multiplicator: Int): CustomBigNumber {
        this.number = number
        this.multiplicator = multiplicator
        return this
    }

    fun add(number: CustomBigNumber): CustomBigNumber {
        val realNumber = number.verify()
        if (realNumber.multiplicator <= this.multiplicator - 3) {
            // Given number is too small, we don't care
            return this
        }
        if (realNumber.multiplicator >= this.multiplicator + 3) {
            // Given number is too big, replace current number by this one
            this.number = realNumber.number
            this.multiplicator = realNumber.multiplicator
            return this
        }
        // We need to convert this number to the same multiplicator
        if (this.multiplicator < realNumber.multiplicator)
            convertTo(realNumber.multiplicator)
        else if (this.multiplicator > realNumber.multiplicator)
            realNumber.convertTo(this.multiplicator)

        // Now we can add this number
        this.number += realNumber.number
        // And, check at the end if we need to increase / decrease this multiplicator
        return verify()
    }

    fun remove(number: CustomBigNumber): CustomBigNumber {
        val realNumber = number.verify()
        if (realNumber.multiplicator <= this.multiplicator - 3) {
            // Given number is too small, we don't care
            return this
        }
        if (realNumber.multiplicator >= this.multiplicator + 3) {
            // Given number is too big, replace current number by 0
            this.number = 0
            this.multiplicator = 1
            return this
        }
        // We need to convert this number to the same multiplicator
        if (this.multiplicator < realNumber.multiplicator)
            convertTo(realNumber.multiplicator)
        else if (this.multiplicator > realNumber.multiplicator)
            realNumber.convertTo(this.multiplicator)

        // Now we can remove this number
        this.number -= realNumber.number
        // And, check at the end if we need to increase / decrease this multiplicator
        return verify()
    }

    /**
     * Verify if this number needs to be increased or decreased
     */
    fun verify(): CustomBigNumber {
        if (number == 0) {
            // This number is 0, we don't care about the multiplicator
            multiplicator = 1
            return this
        }
        while (number >= 1_000_000_000) {
            // We need to increase this number
            number /= 1_000
            multiplicator += 1
        }
        while (multiplicator > 1 && number < 1_000_000) {
            // We need to decrease this number
            number *= 1_000
            multiplicator -= 1
        }
        if (multiplicator == 0) {
            // This number is too small, we need to increase it
            number /= 1_000
            multiplicator += 1
        }
        return this
    }

    fun convertTo(multiply: Int): CustomBigNumber {
        if (multiply == multiplicator)
            return this
        while (multiply > multiplicator) {
            // We need to increase this number
            number /= 1_000
            multiplicator += 1
        }
        while (multiply < multiplicator) {
            // We need to decrease this number
            number *= 1_000
            multiplicator -= 1
        }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CustomBigNumber)
            return false
        val thisVerified = verify()
        val otherVerified = other.verify()
        return thisVerified.number == otherVerified.number && thisVerified.multiplicator == otherVerified.multiplicator
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + multiplicator
        return result
    }

    override fun toString(): String {
        verify()
        if (multiplicator >= MULT_TO_STRING.size - 1)
            return "∞"
        val biggerNumber = number / 1_000_000
        val middleNumber = (number % 1_000_000) / 1_000
        var string = ""
        if (biggerNumber > 0)
            string += "$biggerNumber${MULT_TO_STRING[multiplicator + 1]}"
        if (middleNumber > 0)
            string += "$middleNumber${MULT_TO_STRING[multiplicator]}"
        if (biggerNumber == 0) {
            val smallerNumber = number % 1_000
            if (smallerNumber > 0)
                string += "$smallerNumber${MULT_TO_STRING[multiplicator - 1]}"
        }
        if (string.isEmpty())
            string = "0"
        return string
    }

    companion object {
        val MULT_TO_STRING = arrayOf(
            "", "K", "M", "B", "T",
             "a",  "b",  "c",  "d",  "e",  "f",  "g",  "h",  "i",  "j",  "k",  "l",  "m",  "n",  "o",  "p",  "q",  "r",  "s",  "t",  "u",  "v",  "w",  "x",  "y",  "z",
            "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az",
            "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz",
            "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz",
        )

        /**
         * Convert a string to a CustomBigNumber
         * Example:
         * 1K     -> CustomBigNumber(1_000, 1)
         * 1M     -> CustomBigNumber(1_000_000, 1)
         * 1B     -> CustomBigNumber(1_000_000, 2)
         * 1T     -> CustomBigNumber(1_000_000, 3)
         * 1T475B -> CustomBigNumber(1_475_000, 3)
         */
        fun fromString(string: String): CustomBigNumber? {
            if (string == "∞")
                return CustomBigNumber(1_000_000, 108)
            val customBigNumber = CustomBigNumber(0, 1)
            var number = 0
            var multString = ""
            for (c in string) {
                if (c.isDigit()) {
                    if (multString.isNotEmpty()) {
                        // We need to convert this multString to a number
                        val multNumber = MULT_TO_STRING.indexOf(multString) + 1
                        if (multNumber == 0) {
                            // Invalid multString
                            return null
                        }
                        customBigNumber.add(CustomBigNumber(number, multNumber))
                        multString = ""
                        number = 0
                    }
                    number *= 10
                    number += c.toString().toInt()
                } else {
                    multString += c
                }
            }
            val multNumber = MULT_TO_STRING.indexOf(multString) + 1
            if (multNumber == 0) {
                // Invalid multString
                return null
            }
            return customBigNumber.add(CustomBigNumber(number, multNumber))
        }
    }
}
