package com.pepp.adventofcode

import com.pepp.adventofcode.common.Coordinate
import com.pepp.adventofcode.common.getFileContent
import kotlin.math.abs

fun main() {

    val fileContent = getFileContent("15.txt")

    val regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
    val sensorData = fileContent
        .split("\n")
        .map { line ->
            val (x1, y1, x2, y2) = regex.find(line)!!.destructured
            SensorData(
                Coordinate(x1.toInt(), y1.toInt()),
                Coordinate(x2.toInt(), y2.toInt())
            )
        }

    part1(sensorData)
    part2(sensorData)
}

data class SensorData(
    val sensor: Coordinate,
    val beacon: Coordinate,
    val radius: Int = sensor.manhattanDistanceTo(beacon)
)

private fun part2(sensorData: List<SensorData>) {
    val max = 4000000

    val pointsOfInterest = sensorData.flatMap { sensor ->
        getRim(sensor, max)
    }.groupingBy {
        it
    }
        .eachCount()
        .filter { it.value > 3 }

    println(pointsOfInterest)

    val result = pointsOfInterest
        .map { it.key }
        .filter { point ->
            sensorData.all {
                it.sensor.manhattanDistanceTo(point) > it.radius
            }
        }.map { getFrequency(it) }

    println("Part 2 answer: $result")
}

fun getRim(data: SensorData, max: Int): List<Coordinate> {
    val radius = data.radius + 1
    val right = Coordinate(data.sensor.x + radius, data.sensor.y)
    val bottom = Coordinate(data.sensor.x, data.sensor.y + radius)
    val left = Coordinate(data.sensor.x - radius, data.sensor.y)
    val top = Coordinate(data.sensor.x, data.sensor.y - radius)

    return getEdgeCoordinates(right, bottom, Coordinate(-1, 1))
        .plus(getEdgeCoordinates(bottom, left, Coordinate(-1, -1)))
        .plus(getEdgeCoordinates(left, top, Coordinate(1, -1)))
        .plus(getEdgeCoordinates(top, right, Coordinate(1, 1)))
        .filter { it.x in 0..max && it.y in 0..max }
}

private fun getEdgeCoordinates(
    right: Coordinate,
    left: Coordinate,
    delta: Coordinate
): MutableSet<Coordinate> {
    var c = right
    val result = mutableSetOf<Coordinate>()
    do {
        result.add(c)
        c = c.plus(delta)
    } while (c != left)

    return result
}

private fun getFrequency(it: Coordinate): Long = it.x * 4000000L + it.y

private fun part1(sensorData: List<SensorData>) {
    val row = 2000000

    val sensorsOnRow = sensorData.filter { it.sensor.y == row }.map { it.sensor.x }.toSet()
    val beaconsOnRow = sensorData.filter { it.beacon.y == row }.map { it.beacon.x }.toSet()
    val answer = sensorData.mapNotNull {
        val partOfRadius = it.radius - abs(it.sensor.y - row)
        if (partOfRadius >= 0) {
            it.sensor.x - partOfRadius..it.sensor.x + partOfRadius
        } else null
    }.map { intRange ->
        intRange.toSet()
    }.fold(setOf<Int>()) { acc, ints -> acc.plus(ints) }
        .minus(sensorsOnRow)
        .minus(beaconsOnRow)

    println("Part 1 answer: ${answer.count()}")
}
