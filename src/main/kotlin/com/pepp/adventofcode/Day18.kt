package com.pepp.adventofcode

import com.pepp.adventofcode.common.getFileContent
import kotlin.math.abs

fun main() {

    val fileContent = getFileContent("18.txt")

    val rockPoints = fileContent.split("\n").map {
        val coordinate = it.split(",")
        Point(coordinate[0].toInt(), coordinate[1].toInt(), coordinate[2].toInt())
    }

    val surface = calculateSurface(rockPoints)
    println("Answer 1: $surface")

    val internalPocketCubes = getAirPocketCubes(rockPoints).toMutableList()
    val pocketSurface = calculateSurface(internalPocketCubes)

    println("Answer 2: ${surface - pocketSurface}")
}

private fun calculateSurface(points: List<Point>): Int {
    val surface = points.size * 6
    val subtract = points.sumOf { p ->
        points.count {
            connects(it, p)
        }
    }
    return surface - subtract
}

private fun connects(it: Point, p: Point) =
    (it.x == p.x && it.y == p.y && abs(it.z - p.z) == 1)
            || (it.x == p.x && it.z == p.z && abs(it.y - p.y) == 1)
            || (it.y == p.y && it.z == p.z && abs(it.x - p.x) == 1)


private fun getAirPocketCubes(points: List<Point>): List<Point> {
    val maxPoint = getMaxPoint(points)
    val pocketCubes = mutableSetOf<Point>()
    for (x in 1..maxPoint.x) {
        for (y in 1..maxPoint.y) {
            for (z in 1..maxPoint.z) {
                val point = Point(x, y, z)
                if (!points.contains(point)) {

                    val xAxis = points.filter { it.y == point.y && it.z == point.z }
                    val yAxis = points.filter { it.x == point.x && it.z == point.z }
                    val zAxis = points.filter { it.x == point.x && it.y == point.y }
                    if (xAxis.any { it.x > point.x } && xAxis.any { it.x < point.x }
                        && yAxis.any { it.y > point.y } && yAxis.any { it.y < point.y }
                        && zAxis.any { it.z > point.z } && zAxis.any { it.z < point.z }) {
                        pocketCubes.add(point)
                    }
                }
            }
        }
    }

    return filterOutCubesThatAirCanGetTo(points, maxPoint, pocketCubes)
}

private fun getMaxPoint(rock: List<Point>) =
    Point(
        rock.maxBy { it.x }.x,
        rock.maxBy { it.y }.y,
        rock.maxBy { it.z }.z
    )

private fun filterOutCubesThatAirCanGetTo(
    rockPoints: List<Point>,
    maxPoint: Point,
    cubesToCheck: MutableSet<Point>
): MutableList<Point> {
    val internalPocketCubes = mutableListOf<Point>()
    do {
        val collection = mutableSetOf<Point>()
        if (!findConnectedCubesConnectedToAir(rockPoints, maxPoint, cubesToCheck.first(), collection)) {
            internalPocketCubes.addAll(collection)
        }

        cubesToCheck.removeAll(collection)

    } while (cubesToCheck.isNotEmpty())
    return internalPocketCubes
}

private fun findConnectedCubesConnectedToAir(
    rockPoints: List<Point>,
    maxPoint: Point,
    p: Point,
    collection: MutableSet<Point> = mutableSetOf()
): Boolean {
    val neighbours = setOf(
        Point(p.x, p.y, p.z + 1),
        Point(p.x, p.y, p.z - 1),
        Point(p.x, p.y + 1, p.z),
        Point(p.x, p.y - 1, p.z),
        Point(p.x + 1, p.y, p.z),
        Point(p.x - 1, p.y, p.z),
    )

    val validNeighbours = neighbours.minus(rockPoints).minus(collection)
    collection.addAll(validNeighbours)
    collection.add(p)

    val reachedBorder = validNeighbours.count {
        it.x < 1 ||
                it.y < 1 ||
                it.z < 1 ||
                it.x > maxPoint.x ||
                it.y > maxPoint.y ||
                it.z > maxPoint.z
    }

    if (reachedBorder > 1) {
        return true
    } else {
        validNeighbours.forEach {
            if (findConnectedCubesConnectedToAir(rockPoints, maxPoint, it, collection)) {
                return true
            }
        }
    }

    return false
}

data class Point(
    val x: Int,
    val y: Int,
    val z: Int
)