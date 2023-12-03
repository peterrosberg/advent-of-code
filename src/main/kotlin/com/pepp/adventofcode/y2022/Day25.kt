package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.getFileContent

fun main() {

    val fileContent = getFileContent("2022/25.txt")

    val snafus = fileContent.split("\n").map {
        Snafu(it)
    }

    val total = snafus.sumOf { it.toLong() }

    println("Total: $total")
    println("Total as Snafu: ${Snafu.valueOf(total)}")

}

data class Snafu(
    val s: String
) {
    fun toLong(): Long {
        val string = s.reversed()

        var placeFactor = 1L
        var output = 0L
        for (element in string) {
            val factor = when (element) {
                '=' -> -2
                '-' -> -1
                '0' -> 0
                '1' -> 1
                '2' -> 2
                else -> throw IllegalArgumentException("Invalid SNAFU number $s")
            }
            output += factor * placeFactor
            placeFactor *= 5
        }
        return output
    }

    companion object {
        fun valueOf(decimalValue: Long): Snafu {
            val stringBuffer = StringBuffer("")
            var left = decimalValue
            var factor = 1L
            do {
                val mod = (left / factor) % 5
                when (mod) {
                    0L -> stringBuffer.append('0')
                    1L -> stringBuffer.append('1')
                    2L -> stringBuffer.append('2')
                    3L -> {
                        stringBuffer.append('=')
                        left += factor * 5
                    }

                    4L -> {
                        stringBuffer.append('-')
                        left += factor * 5
                    }
                }
                left -= mod * factor
                factor *= 5
            } while (left > 0)

            return Snafu(stringBuffer.reversed().toString())
        }
    }

    override fun toString(): String = s
}