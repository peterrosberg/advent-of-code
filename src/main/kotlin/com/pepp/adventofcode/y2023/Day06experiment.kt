package com.pepp.adventofcode.y2023

import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.math.truncate
import kotlin.system.measureTimeMillis

fun main() {
    val oneTime = 56977793L
    val oneDistance = 499221010971440L

    val timeInMillis1 = measureTimeMillis {
        val part2 = calculateRaceWithLoop(oneTime, oneDistance)
        println(part2)
    }
    println("Took $timeInMillis1 ms to run with loop")

    val timeInMillis2 = measureTimeMillis {
        val part2 = calculateRace(oneTime, oneDistance)
        println(part2)
    }
    println("Took $timeInMillis2 ms to run without loop")
}

private fun calculateRace(
    time: Long, dist: Long
): Int {
    val minTime = (-time + sqrt(0.0 + time * time - 4 * dist)) / -2
    val maxTime = (-time - sqrt(0.0 + time * time - 4 * dist)) / -2

    return (ceil(maxTime) - truncate(minTime) - 1).toInt()
}

private fun calculateRaceWithLoop(
    time: Long, record: Long
): Int {
    var numRecords = 0
    for (holdTime in 1..time) {
        val raceTime = time - holdTime
        val dist = raceTime * holdTime

        if (dist > record) {
            numRecords += 1;
        }
    }
    return numRecords
}