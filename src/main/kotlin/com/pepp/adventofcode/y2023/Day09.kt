package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day09.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day09 {
    fun main() {
        val fileContent = getFileContent("2023/9.txt")

        val numbersList = fileContent.split("\n").map { row ->
            row.split(" ").map { it.toInt() }
        }

        val part1 = numbersList.map { numbers ->
            val nextNumber = getNextNumber(numbers)
            nextNumber
        }.sum()

        val part2 = numbersList.map { numbers ->
            val nextNumber = getPreviousNumber(numbers)
            nextNumber
        }.sum()

        println(part2)
    }

    private fun getPreviousNumber(numbers: List<Int>): Int {
        if (numbers.all { it == 0 }) return 0
        val diffs = getDiffs(numbers)

        return numbers.first() - getPreviousNumber(diffs)
    }

    private fun getNextNumber(numbers: List<Int>): Int {
        if (numbers.all { it == 0 }) return 0
        val diffs = getDiffs(numbers)

        return numbers.last() + getNextNumber(diffs)
    }

    private fun getDiffs(numbers: List<Int>): List<Int> {
        val output = mutableListOf<Int>()
        for (i in 1 until numbers.size) {
            output.add(numbers[i] - numbers[i - 1])
        }
        return output.toList()
    }

}
