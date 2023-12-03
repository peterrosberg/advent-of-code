package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.Coordinate
import com.pepp.adventofcode.common.getFileContent

private val rocks = listOf(
    listOf(
        Coordinate(0, 0),
        Coordinate(1, 0),
        Coordinate(2, 0),
        Coordinate(3, 0),
    ),
    listOf(
        Coordinate(1, 0),
        Coordinate(0, 1),
        Coordinate(1, 1),
        Coordinate(2, 1),
        Coordinate(1, 2),
    ),
    listOf(
        Coordinate(0, 0),
        Coordinate(1, 0),
        Coordinate(2, 0),
        Coordinate(2, 1),
        Coordinate(2, 2),
    ),
    listOf(
        Coordinate(0, 0),
        Coordinate(0, 1),
        Coordinate(0, 2),
        Coordinate(0, 3),
    ),
    listOf(
        Coordinate(0, 0),
        Coordinate(0, 1),
        Coordinate(1, 0),
        Coordinate(1, 1),
    )
)

fun main() {

    val fileContent = getFileContent("2022/17.txt")

    val answer1 = calculateRockHeight(fileContent, 2022L)
    println("Answer part 1: $answer1 ")

    val answer2 = calculateRockHeight(fileContent, 1000000000000L)
    println("Answer part 2: $answer2 ")
}

private fun calculateRockHeight(fileContent: String, targetRockCount: Long): Long {
    var rockIteration = 0L
    var currentRock = applyRock(Coordinate(3, 4), rocks[0])

    var currentHeight = 0

    val cave = mutableSetOf<Coordinate>()
    val seen = mutableMapOf<Pair<Int, Int>, Pair<Long, Long>>()

    var jetIndex = 0
    while (rockIteration < targetRockCount) {
        val direction = getDirection(fileContent, jetIndex)
        currentRock = moveRock(direction, currentRock, cave)

        val nextRock = moveRock(Coordinate(0, -1), currentRock, cave)
        if (nextRock == currentRock) {
            cave.addAll(currentRock)
            currentHeight = findStartHeight(cave)
            val rockIndex = ((++rockIteration) % rocks.size).toInt()
            currentRock = applyRock(Coordinate(3, currentHeight + 4), rocks[rockIndex])

            if (!seen.containsKey(Pair(rockIndex, jetIndex))) {
                seen[Pair(rockIndex, jetIndex)] = Pair(rockIteration, currentHeight.toLong())
            } else {
                val (pastRock, pastHeight) = seen[Pair(rockIndex, jetIndex)]!!
                val rockCycle = rockIteration - pastRock

                if (rockIteration % rockCycle == targetRockCount % rockCycle) {
                    val heightCycle = currentHeight - pastHeight

                    val remainingCycles = ((targetRockCount - rockIteration) / rockCycle)
                    return remainingCycles * heightCycle + currentHeight
                }
            }
        } else {
            currentRock = nextRock
        }

        jetIndex = (jetIndex + 1) % fileContent.length
    }

    return currentHeight.toLong()
}

private fun findStartHeight(cave: Set<Coordinate>) = cave.maxBy { it.y }.y

private fun moveRock(
    direction: Coordinate,
    currentRock: List<Coordinate>,
    cave: Set<Coordinate>
): List<Coordinate> {
    val nextRockPosition = applyRock(direction, currentRock)
    return if (nextRockPosition.intersect(cave).isEmpty() && !hasHitWallsOrBottom(nextRockPosition)) {
        nextRockPosition
    } else currentRock
}

private fun hasHitWallsOrBottom(nextRockPosition: List<Coordinate>) =
    nextRockPosition.any {
        it.x <= 0 || it.x > 7 || it.y <= 0
    }

private fun getDirection(fileContent: String, jetIndex: Int): Coordinate = when (fileContent[jetIndex]) {
    '<' -> Coordinate(-1, 0)
    '>' -> Coordinate(1, 0)
    else -> throw RuntimeException("Illegal character ${fileContent[jetIndex]}")
}

private fun applyRock(position: Coordinate, rock: List<Coordinate>): List<Coordinate> = rock.map {
    it.plus(position)
}