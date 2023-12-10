package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.math.truncate
import kotlin.system.measureTimeMillis

fun main() {
    val timeInMillis = measureTimeMillis {
        val fileContent = getFileContent("2023/6.txt")

        val sections = fileContent.split("\n")

        val times = sections[0].substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }
        val distances = sections[1].substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }

        val part1 = calculateRace(times, distances)
        println(part1)

        val oneTime = times.joinToString(separator = "")
        val oneDistance = distances.joinToString(separator = "")

        val part2 = calculateRace(listOf(oneTime), listOf(oneDistance))
        println(part2)
    }
    println("Took $timeInMillis ms to run")
}

private fun calculateRace(
    times: List<String>,
    distances: List<String>
): Int {
    var sum = 1
    for (i in times.indices) {
        val time = times[i].toLong()
        val dist = distances[i].toLong()

        val minTime = (-time + sqrt(0.0 + time * time - 4 * dist)) / -2
        val maxTime = (-time - sqrt(0.0 + time * time - 4 * dist)) / -2

        val numberOfWays = ceil(maxTime) - truncate(minTime) - 1
        sum *= numberOfWays.toInt()
    }
    return sum
}
