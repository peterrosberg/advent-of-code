package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day14.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day14 {

    fun main() {
        val fileContent = getFileContent("2023/14.txt")

        val rocks = fileContent.split("\n").flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                when (c) {
                    'O' -> Pair(x, y) to true
                    '#' -> Pair(x, y) to false
                    else -> null
                }
            }
        }.associate { it.first to it.second }

        val maxX = rocks.keys.maxOf { it.first }
        val maxY = rocks.keys.maxOf { it.second }

        val north = 0 to -1
        val rocksMoved = moveAllRocks(rocks, north, maxX, maxY)

        val part1 = calculateLoad(rocksMoved)
        println(part1)

        var direction = north
        var currentState = rocks
        var counter = 0
        val cache: MutableMap<Set<Pair<Int, Int>>, Int> = mutableMapOf()
        do {
            counter++
            //printRocks(currentState)
            repeat((1..4).count()) {
                currentState = moveAllRocks(currentState, direction, maxX, maxY)
                direction = turnDirectionLeft(direction)
            }
            val cacheHit = cache[currentState.keys]
            if (cacheHit != null) {
                println("Cache hit $counter == $cacheHit")
                val loop = counter - cacheHit
                if ((1000000000 - counter).mod(loop) == 0) break
            } else {
                cache[currentState.keys] = counter
            }
        } while (true)

        //printRocks(currentState)

        val part2 = calculateLoad(currentState)
        println(part2)
        //93316 too high
    }

    private fun moveAllRocks(
        rocks: Map<Pair<Int, Int>, Boolean>,
        direction: Pair<Int, Int>,
        maxX: Int,
        maxY: Int
    ): Map<Pair<Int, Int>, Boolean> {
        var rocksMoved = Pair(rocks, 999)
        while (rocksMoved.second > 0) {
            //printRocks(rocksMoved.first)
            rocksMoved = moveRocks(rocksMoved.first, direction, maxX, maxY)
            //println("Rocks moved: ${rocksMoved.second}")
        }
        return rocksMoved.first
    }

    private fun turnDirectionLeft(direction: Pair<Int, Int>): Pair<Int, Int> = Pair(direction.second, -direction.first)

    private fun calculateLoad(rocks: Map<Pair<Int, Int>, Boolean>): Any {
        val maxLoad = rocks.keys.maxOf { it.second } + 1
        return rocks.filter { it.value }.keys.sumOf {
            maxLoad - it.second
        }
    }

    private fun moveRocks(
        rocks: Map<Pair<Int, Int>, Boolean>,
        direction: Pair<Int, Int>,
        maxX: Int,
        maxY: Int
    ): Pair<MutableMap<Pair<Int, Int>, Boolean>, Int> {
        val moved = mutableMapOf<Pair<Int, Int>, Boolean>()
        var movements = 0
        for (key in rocks.keys) {
            if (rocks[key] == true) {
                val moveTo = key.first + direction.first to key.second + direction.second
                if (moveTo.first >= 0 && moveTo.second >= 0 && moveTo.first <= maxX && moveTo.second <= maxY && rocks[moveTo] == null) {
                    moved[moveTo] = true
                    movements++
                } else {
                    moved[key] = true
                }
            } else {
                moved[key] = false
            }
        }
        //println("$movements movements")
        return moved to movements
    }

    private fun printRocks(rocks: Map<Pair<Int, Int>, Boolean>) {
        val maxX = rocks.keys.maxOf { it.first }
        val maxY = rocks.keys.maxOf { it.second }

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                val value = rocks[x to y]
                when (value) {
                    null -> print('.')
                    true -> print('O')
                    else -> print('#')
                }
            }
            println()
        }
    }

}
