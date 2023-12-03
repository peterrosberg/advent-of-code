package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import com.pepp.adventofcode.y2023.CubeGameColor.*
import kotlin.math.max

fun main() {

    val fileContent = getFileContent("2023/2.txt")

    val cubeGames = fileContent.split("\n")
        .map { row -> CubeGame(row) }

    val part1 = cubeGames
        .filter { it.isValid() }
        .sumOf { it.number }

    val part2 = cubeGames
        .sumOf { it.getPower() }

    println(part1)
    println(part2)

}

class CubeGameSet(input: String) {
    private val cubes: Map<CubeGameColor, Int>

    init {
        cubes = CubeGameColor.values().associateWith {
            "(\\d+) ${it.name.lowercase()}".toRegex().find(input)?.groupValues?.get(1)?.toInt() ?: 0
        }
    }

    fun cubes(color: CubeGameColor) = cubes[color] ?: 0
}

class CubeGame(input: String) {
    fun isValid(): Boolean =
        !sets.any { (it.cubes(RED)) > 12 || (it.cubes(GREEN)) > 13 || (it.cubes(BLUE)) > 14 }

    fun getPower(): Int = CubeGameColor.values().map { color ->
        sets.fold(0) { acc, set -> max(acc, set.cubes(color)) }
    }.fold(1) { acc, number -> number * acc }

    val number: Int
    private val sets: List<CubeGameSet>

    init {
        number = "Game (\\d+):".toRegex().find(input)?.groupValues?.get(1)?.toInt() ?: 0
        sets = input.substringAfter(":").split(";").map { CubeGameSet(it) }
    }
}

enum class CubeGameColor {
    RED, GREEN, BLUE;
}

