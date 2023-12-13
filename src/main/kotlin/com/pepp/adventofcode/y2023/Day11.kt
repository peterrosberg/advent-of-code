package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day11.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day11 {


    fun main() {
        val fileContent = getFileContent("2023/11.txt")

        val galaxies = fileContent.split("\n")
            .flatMapIndexed { y: Int, row: String ->
                row.mapIndexedNotNull { x, c ->
                    if (c == '#') Coordinate(x.toLong(), y.toLong()) else null
                }
            }

        val expandedUniverse = expandUniverse(galaxies, 2)

        val part1 = totalDistance(expandedUniverse)
        println(part1)

        val expandedUniverse2 = expandUniverse(galaxies, 1000000)

        val part2 = totalDistance(expandedUniverse2)
        println(part2)
    }

    private fun expandUniverse(galaxies: List<Coordinate>, scale: Int): List<Coordinate> {
        val columnsWithoutGalaxies = findEmptyLinesBy(galaxies) { it.x }
        val rowsWithoutGalaxies = findEmptyLinesBy(galaxies) { it.y }

        val expandedUniverse = galaxies.map { galaxy ->
            Coordinate(
                galaxy.x + columnsWithoutGalaxies.count { it < galaxy.x } * (scale - 1),
                galaxy.y + rowsWithoutGalaxies.count { it < galaxy.y } * (scale - 1)
            )
        }
        return expandedUniverse
    }

    private fun totalDistance(expandedUniverse: List<Coordinate>): Long {
        var totalDistance = 0L
        for (i in expandedUniverse.indices)
            for (j in i + 1 until expandedUniverse.size) {
                val one = expandedUniverse[i]
                val two = expandedUniverse[j]
                val distanceTo = one.manhattanDistanceTo(two)
                //println("distance between $one and $two = $distanceTo")
                totalDistance += distanceTo
            }
        return totalDistance
    }

    private fun findEmptyLinesBy(galaxies: List<Coordinate>, transformFn: (Coordinate) -> Long): List<Long> {
        val withGalaxies = galaxies.associateBy(transformFn).keys
        val originalMax = galaxies.maxOf(transformFn)

        return (0..originalMax).mapNotNull {
            if (withGalaxies.contains(it)) null else it
        }
    }

    data class Coordinate(
        val x: Long,
        val y: Long
    ) {
        fun manhattanDistanceTo(other: Coordinate): Long {
            return abs(x - other.x) + abs(y - other.y)
        }
    }
}
