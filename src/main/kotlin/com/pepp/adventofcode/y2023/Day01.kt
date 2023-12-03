package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent

fun main() {

    val fileContent = getFileContent("2023/1.txt")

    val part1 = fileContent.split("\n")
        .map { row ->
            val first = row.find { it.isDigit() }
            val last = row.findLast { it.isDigit() }
            "$first$last"
        }
        .sumOf { it.toInt() }

    println(part1)

    val digitPattern = "(?=(\\d|one|two|three|four|five|six|seven|eight|nine)).".toRegex()

    val part2 = fileContent.split("\n")
        .map { row ->
            val allMatches = digitPattern.findAll(row)
            val first = parseDigit(allMatches.first().groupValues[1])
            val last = parseDigit(allMatches.last().groupValues[1])
            val digit = "$first$last"
            println(digit)
            digit
        }
        .sumOf { it.toInt() }

    println(part2)
}

fun parseDigit(result: String): Int = when (result) {
    "one" -> 1
    "two" -> 2
    "three" -> 3
    "four" -> 4
    "five" -> 5
    "six" -> 6
    "seven" -> 7
    "eight" -> 8
    "nine" -> 9
    else -> result.toInt()
}


