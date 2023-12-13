package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day12.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day12 {
    fun main() {
        val fileContent = getFileContent("2023/12.txt")

        val allRows = fileContent.split("\n")
        val rows = allRows.map { row ->
            val (str, numbers) = row.split(" ")
            val configuration = Configuration(numbers.split(",").map { it.toInt() })
            Pair(str, configuration)
        }

        val part1 = rows.sumOf { (str, configuration) ->
            configuration.numberOfMatches(str)
        }
        println(part1)

        val rowsPart2 = unfold(rows)
        val part2 = rowsPart2.sumOf { (str, configuration) ->
            configuration.numberOfMatches(str)
        }
        println(part2)
    }

    private fun unfold(rows: List<Pair<String, Configuration>>): List<Pair<String, Configuration>> =
        rows.map { (str, configuration) ->
            val newStr = (1..5).joinToString(separator = "?") { str }
            val newConfiguration = Configuration((1..5).flatMap { configuration.groups })
            println("$newStr ${newConfiguration.groups}")
            Pair(newStr, newConfiguration)
        }


    class Configuration(
        val groups: List<Int>
    ) {
        private val initialSpace: MutableList<Int> = groups.map { 1 }.toMutableList()

        init {
            initialSpace[0] = 0
        }

        fun numberOfMatches(
            str: String,
            level: Int = initialSpace.size - 1,
            spaceConfiguration: MutableList<Int> = initialSpace
        ): Int {
            if (groups.sum() + spaceConfiguration.sum() > str.length) return 0
            //print(spaceConfiguration)

            var matches = 0
            if (matches(str, spaceConfiguration)) {
                //println("valid!")
                print(spaceConfiguration, str.length)
                matches++
            }

            for (l in level downTo 0) {
                val copy = spaceConfiguration.toMutableList()
                copy[l]++
                matches += numberOfMatches(str, l, copy)
            }
            return matches

        }

        private fun print(spaces: MutableList<Int>, length: Int) {
            var str = ""
            for (i in groups.indices) {
                val spaceCount = spaces[i]
                val group = groups[i]
                for (j in 0 until spaceCount) {
                    str += "."
                }
                for (j in 0 until group) {
                    str += '#'
                }
            }
            str = str.padEnd(length, '.')
            println(str)
        }

        fun matches(str: String, spaceList: MutableList<Int>): Boolean {

            if (groups.sum() + spaceList.sum() > str.length) return false

            var iter = 0
            for (i in groups.indices) {
                val spaces = spaceList[i]
                val group = groups[i]
                for (j in 0 until spaces) {
                    if (str[iter++] == '#') {
                        return false
                    }
                }
                for (j in 0 until group) {
                    if (str[iter++] == '.') {
                        return false
                    }
                }
            }

            while (iter < str.length) {
                if (str[iter++] == '#') {
                    return false
                }
            }

            return true
        }

    }
}
