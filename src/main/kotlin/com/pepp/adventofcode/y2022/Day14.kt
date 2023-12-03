package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.Coordinate
import com.pepp.adventofcode.common.getFileContent
import kotlin.math.max
import kotlin.math.min

fun main() {

    val fileContent = getFileContent("2022/14.txt")

    val startPoint = Coordinate(500, 0)
    val cave = getCaveSystem(fileContent)
    val originalCaveSize = cave.size
    cave[startPoint] = "+"

    println(cave)

    val maxX = cave.maxBy { it.key.x }.key.x
    val minX = cave.minBy { it.key.x }.key.x
    val maxY = cave.maxBy { it.key.y }.key.y
    val minY = cave.minBy { it.key.y }.key.y

    printCave(minY, maxY, minX, maxX, cave)

    var currentPath = mutableListOf(startPoint)

    do {
        currentPath = trackGrain(currentPath, cave, maxY + 1)
        val resting = currentPath.removeLast()
        if (resting.y <= maxY) cave[resting] = "o"
    } while (resting.y <= maxY)

    printCave(minY, maxY, minX, maxX, cave)

    println("Answer 1: ${cave.size - originalCaveSize - 1}")

    val floorRest = maxY + 1

    do {
        currentPath = trackGrain(currentPath, cave, floorRest)
        val resting = currentPath.removeLast()
        cave[resting] = "o"
        if (cave.size % 5000 == 0) printCave(minY, floorRest + 2, minX - 50, maxX + 50, cave)
    } while (currentPath.isNotEmpty())

    printCave(minY, floorRest + 2, minX - 50, maxX + 50, cave)

    println("Answer 2: ${cave.size - originalCaveSize}")
}

private fun getCaveSystem(fileContent: String) = fileContent.split("\n")
    .map { path ->
        path.split(" -> ").map { coord ->
            Coordinate(
                coord.split(",")[0].toInt(),
                coord.split(",")[1].toInt()
            )
        }
    }.flatMap { path ->
        val rockPoints = mutableSetOf<Coordinate>()
        for (i in 1 until path.size) {
            val prev = path[i - 1]
            val curr = path[i]

            if (prev.x == curr.x) {
                for (y in min(prev.y, curr.y)..max(prev.y, curr.y)) {
                    rockPoints.add(Coordinate(curr.x, y))
                }
            } else {
                for (x in min(prev.x, curr.x)..max(prev.x, curr.x)) {
                    rockPoints.add(Coordinate(x, curr.y))
                }
            }
        }
        rockPoints.toList()
    }
    .associateWith { "#" }
    .toMutableMap()

private fun printCave(
    minY: Int,
    maxY: Int,
    minX: Int,
    maxX: Int,
    cave: MutableMap<Coordinate, String>
) {
    println("===================================== Cave size: ${cave.size}")
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (cave.contains(Coordinate(x, y))) {
                print(cave[Coordinate(x, y)])
            } else {
                if (x % 10 == 0) print(",") else print(".")
            }
        }
        println(" $y")
    }
}

private fun trackGrain(
    currentPath: MutableList<Coordinate>,
    cave: MutableMap<Coordinate, String>,
    maxY: Int
): MutableList<Coordinate> {
    var grain = currentPath.last()
    do {
        val nextGrain = moveGrain(grain, cave)
        if (nextGrain == grain) {
            break
        }
        currentPath.add(nextGrain)
        grain = nextGrain
    } while (grain.y < maxY)
    return currentPath
}

fun moveGrain(grain: Coordinate, cave: MutableMap<Coordinate, String>): Coordinate {
    val down = grain.plus(Coordinate(0, 1))
    if (cave.contains(down)) {
        val left = grain.plus(Coordinate(-1, 1))
        if (cave.contains(left)) {
            val right = grain.plus(Coordinate(1, 1))
            if (cave.contains(right)) {
                return grain
            }
            return right
        }
        return left
    }
    return down
}

