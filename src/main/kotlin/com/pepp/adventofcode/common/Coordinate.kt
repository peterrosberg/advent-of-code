package com.pepp.adventofcode.common

import kotlin.math.abs

/**
 * Useful class for keeping track of Coordinates
 */
data class Coordinate(
    val x: Int,
    val y: Int
) {
    fun plus(other: Coordinate): Coordinate {
        return Coordinate(x + other.x, y + other.y)
    }

    fun minus(other: Coordinate): Coordinate {
        return Coordinate(x - other.x, y - other.y)
    }
    
    fun manhattanDistanceTo(other: Coordinate): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    companion object {
        val RIGHT = Coordinate(1, 0)
        val LEFT = Coordinate(-1, 0)
        val UP = Coordinate(0, -1)
        val DOWN = Coordinate(0, 1)
        val ALL_DIRECTIONS = listOf(RIGHT, LEFT, UP, DOWN)
    }
}