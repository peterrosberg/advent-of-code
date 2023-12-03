package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.Coordinate
import com.pepp.adventofcode.common.getFileContent
import kotlin.math.max
import kotlin.math.min

fun main() {

    val fileContent = getFileContent("2023/3.txt")

    val rows = fileContent.split("\n")

    var ongoingDigit = ""
    var part1Sum = 0
    val gearMap = mutableMapOf<Coordinate, List<Int>>()

    val maxX = rows[0].length - 1
    val maxY = rows.size - 1
    for (y in 0..maxY) {
        for (x in 0..maxX) {
            val c = rows[y][x]
            if (c.isDigit()) {
                ongoingDigit += c
            }
            if ((!c.isDigit() || x == maxX) && ongoingDigit != "") {
                if (hasAdjacentSymbol(rows, ongoingDigit, x, maxX, y, maxY)) {
                    part1Sum += ongoingDigit.toInt()
                }
                val gears = findAdjacentGears(rows, ongoingDigit, x, maxX, y, maxY)
                gears.forEach {
                    gearMap.compute(it) { _, v -> v?.plus(ongoingDigit.toInt()) ?: listOf(ongoingDigit.toInt()) }
                }
                ongoingDigit = ""
            }
        }
    }

    println(part1Sum)

    val part2 = gearMap
        .filterValues { it.size > 1 }
        .map { it.value.fold(1) { acc, v -> v * acc } }
        .sum()

    println(part2)

    //507214
    //72553319
}

private fun hasAdjacentSymbol(
    rows: List<String>,
    ongoingDigit: String,
    x: Int,
    maxX: Int,
    y: Int,
    maxY: Int
): Boolean {
    for (xs in max(x - ongoingDigit.length - 1, 0)..min(x, maxX))
        for (ys in max(y - 1, 0)..min(y + 1, maxY)) {
            if (rows[ys][xs] != '.' && !rows[ys][xs].isDigit()) {
                return true
            }
        }
    return false
}


private fun findAdjacentGears(
    rows: List<String>,
    ongoingDigit: String,
    x: Int,
    maxX: Int,
    y: Int,
    maxY: Int
): List<Coordinate> {
    val gears = mutableListOf<Coordinate>()
    for (xs in max(x - ongoingDigit.length - 1, 0)..min(x, maxX))
        for (ys in max(y - 1, 0)..min(y + 1, maxY)) {
            if (rows[ys][xs] == '*') {
                gears.add(Coordinate(xs, ys))
            }
        }
    return gears
}

