package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.Coordinate
import com.pepp.adventofcode.common.getFileContent
import kotlin.math.abs

fun main() {

    val fileContent = getFileContent("2022/09.txt")

    val visitedPositions = mutableSetOf<Coordinate>()
    val rope = MutableList(10) { Coordinate(0, 0) }

    fileContent.split("\n").forEach { cmd ->

        val length = cmd.split(" ")[1].toInt()

        val direction = getDirection(cmd.split(" ")[0])

        for (i in 1..length) {
            rope[0] = rope[0].plus(direction)
            for (knot in 1 until rope.size) {
                rope[knot] = calculateTailPosition(rope[knot - 1], rope[knot])
            }
            visitedPositions.add(rope.last())
            println(rope.last())
        }
    }

    println(visitedPositions.size)
}

fun calculateTailPosition(headPos: Coordinate, tailPos: Coordinate): Coordinate {

    val diff = headPos.minus(tailPos)
    if (abs(diff.x) > 1 || abs(diff.y) > 1) {
        val tailMovement = Coordinate(
            constrainValue(diff.x),
            constrainValue(diff.y)
        )
        return tailPos.plus(tailMovement)
    }

    return tailPos
}

private fun constrainValue(value: Int) = if (value == 0) 0 else value / abs(value)

private fun getDirection(str: String) = when (str) {
    "R" -> Coordinate.RIGHT
    "L" -> Coordinate.LEFT
    "U" -> Coordinate.UP
    else -> Coordinate.DOWN
}