package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day12b.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day12b {
    fun main() {
        val fileContent = getFileContent("2023/12.txt")

        val rows = fileContent.split("\n").map { row ->
            val (str, numbers) = row.split(" ")
            val configuration = numbers.split(",").map { it.toInt() }
            Pair(str, configuration)
        }

        val part1 = sumAllCombinations(rows)
        println(part1)

        val part2 = sumAllCombinations(unfold(rows))
        println(part2)
    }

    private fun sumAllCombinations(rows: List<Pair<String, List<Int>>>) =
        rows.sumOf { (str, configuration) ->
            var matchingVariants = 0
            val nrOfWildCards = str.count { it == '?' }
            val max = 2.0.pow(nrOfWildCards).toInt()
            for (i in 0 until max) {
                val variations = Integer.toBinaryString(i).padStart(nrOfWildCards, '0').reversed()
                var c = 0
                val variant = str.map {
                    if (it == '?') {
                        if (variations[c++] == '0') '#' else '.'
                    } else {
                        it
                    }
                }.joinToString(separator = "")

                val myCount = countGroups(variant)
                //println("$variant - $configuration = $myCount")
                if (myCount == configuration) {
                    matchingVariants++
                }

            }
            println("$configuration -> $matchingVariants")
            matchingVariants

        }

    private fun unfold(rows: List<Pair<String, List<Int>>>): List<Pair<String, List<Int>>> =
        rows.map { (str, configuration) ->
            val newStr = (1..5).joinToString(separator = "?") { str }
            val newConfiguration = (1..5).flatMap { configuration }
            println("$newStr $newConfiguration")
            Pair(newStr, newConfiguration)
        }

    fun countGroups(variant: String): List<Int> {
        return variant.split("\\.+".toRegex()).map { it.length }.filter { it != 0 }
    }

}
