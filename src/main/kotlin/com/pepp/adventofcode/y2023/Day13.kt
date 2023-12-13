package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day13.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day13 {
    
    fun main() {
        val fileContent = getFileContent("2023/13.txt")

        val maps = fileContent.split("\n\n")
            .map { map ->
                MapMatrix(map.split("\n"))
            }

        val part1 = findReflections(maps, 0)
        println(part1)
        val part2 = findReflections(maps, 1)
        println(part2)
    }

    private fun findReflections(maps: List<MapMatrix>, corrections: Int) =
        maps.mapIndexed { index, map ->

            val horizontalReflection = findReflection(map.rows, corrections)
            val verticalReflection = findReflection(map.cols, corrections)

            horizontalReflection * 100 + verticalReflection

        }.sum()

    private fun findReflection(lines: List<String>, corrections: Int): Int {
        for (i in lines.indices) {
            if (testReflection(lines, i, corrections)) {
                return i
            }
        }
        return 0
    }

    private fun testReflection(pattern: List<String>, i: Int, wantedCorrections: Int): Boolean {
        if (i == 0) return false

        var corrections = 0
        var j = 0
        while (i + j < pattern.size && i - 1 - j >= 0) {
            corrections += compare(pattern[i + j], pattern[i - 1 - j])
            if (corrections > wantedCorrections) return false
            j++
        }

        return corrections == wantedCorrections
    }

    private fun compare(str1: String, str2: String): Int {
        return str1.indices.map { if (str1[it] == str2[it]) 0 else 1 }.sum()
    }

    class MapMatrix(val rows: List<String>) {
        val cols: List<String>

        init {
            val tempCol = (0 until rows.first().length).map { "" }.toMutableList()
            rows.forEach { row ->
                row.forEachIndexed { index, c ->
                    tempCol[index] = tempCol[index] + c
                }
            }
            cols = tempCol.toList()
        }
    }
}
