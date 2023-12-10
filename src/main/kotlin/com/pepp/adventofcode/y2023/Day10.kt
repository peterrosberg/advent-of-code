package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import com.pepp.adventofcode.y2023.Day10.Direction.Companion.EAST
import com.pepp.adventofcode.y2023.Day10.Direction.Companion.NORTH
import com.pepp.adventofcode.y2023.Day10.Direction.Companion.SOUTH
import com.pepp.adventofcode.y2023.Day10.Direction.Companion.WEST
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day10.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day10 {

    fun main() {
        val fileContent = getFileContent("2023/10.txt")

        val grid = Grid(fileContent.split("\n"))

        val startNode = grid.findStart()
        startNode.distance = 0

        var distance = 1
        var ongoing = grid.findNodesConnectedTo(startNode.coordinate)

        while (ongoing.isNotEmpty()) {
            ongoing = calculateDistances(grid, ongoing, distance)
            distance++
        }

        println(grid.findMaxDistance())

        //traverse the loop
        traverseLoopAndMarkHolesToLeftAndRight(grid, startNode)

        //assumption that the first row contains some hole which is on the outside of the loop
        val outSideGroup = grid.rows.first().find { it.holeGroup != HoleGroup.NONE }!!.holeGroup
        val insideGroup = if (outSideGroup == HoleGroup.LEFT) HoleGroup.RIGHT else HoleGroup.LEFT

        val part2 = grid.findAll {
            it.holeGroup == insideGroup
        }.count()

        println(part2)

    }

    private fun traverseLoopAndMarkHolesToLeftAndRight(grid: Grid, startNode: Node) {
        var currenNode = grid.findNodesConnectedTo(startNode.coordinate).first()
        var direction = currenNode.coordinate.directionFrom(startNode.coordinate)


        while (currenNode != startNode) {
            //println("Current Node = $currenNode")
            //println("Direction = $direction")

            var nextNode: Node?
            if (currenNode.char == '|' || currenNode.char == '-') {
                //continue straight
                handlePotentialHole(grid, currenNode, direction.turnLeft(), HoleGroup.LEFT)
                handlePotentialHole(grid, currenNode, direction.turnRight(), HoleGroup.RIGHT)
                nextNode = grid.get(currenNode.coordinate.move(direction))
            } else if ((direction == NORTH && currenNode.char == 'F')
                || (direction == WEST && currenNode.char == 'L')
                || (direction == SOUTH && currenNode.char == 'J')
                || (direction == EAST && currenNode.char == '7')
            ) {
                //turning right
                handlePotentialHole(grid, currenNode, direction.turnLeft(), HoleGroup.LEFT)
                handlePotentialHole(grid, currenNode, direction, HoleGroup.LEFT)
                nextNode = grid.get(currenNode.coordinate.move(direction.turnRight()))
            } else {
                //turning left
                handlePotentialHole(grid, currenNode, direction.turnRight(), HoleGroup.RIGHT)
                handlePotentialHole(grid, currenNode, direction, HoleGroup.RIGHT)
                nextNode = grid.get(currenNode.coordinate.move(direction.turnLeft()))
            }


            if (currenNode.char == '.') break //circuit breaker in case we f**cked up
            direction = nextNode!!.coordinate.directionFrom(currenNode.coordinate)
            currenNode = nextNode
        }
    }

    private fun handlePotentialHole(grid: Grid, nextNode: Node, direction: Direction, holeGroup: HoleGroup) {
        handlePotentialHole(grid, grid.get(nextNode.coordinate.move(direction)), holeGroup)
    }

    private fun handlePotentialHole(grid: Grid, node: Node?, group: HoleGroup) {
        if (node == null || node.distance >= 0 || node.holeGroup != HoleGroup.NONE) return

        node.holeGroup = group
        //spread in all directions
        node.coordinate.allNeighbours().forEach {
            handlePotentialHole(grid, grid.get(it), group)
        }
    }

    private fun calculateDistances(grid: Grid, nodes: List<Node>, distance: Int): List<Node> {
        val continueWith = mutableListOf<Node>()
        nodes.forEach {
            if (it.distance > distance || it.distance < 0) {
                it.distance = distance
                continueWith.addAll(grid.getNextNodes(it.coordinate))
            }
        }
        return continueWith.toList()
    }

    class Grid(rows: List<String>) {

        val rows: List<List<Node>>

        init {
            this.rows = rows.mapIndexed { y, row -> row.mapIndexed { x, c -> Node(c, Coordinate(x, y)) } }
        }

        fun get(coordinate: Coordinate): Node? {
            if (coordinate.y >= 0 && coordinate.y < rows.size)
                if (coordinate.x >= 0 && coordinate.x < rows[coordinate.y].size) {
                    return rows[coordinate.y][coordinate.x]
                }
            //println("Failed getting $coordinate")
            return null
        }

        fun getNextNodes(coordinate: Coordinate): List<Node> {
            return when (get(coordinate)?.char) {
                '|' -> coordinate.move(NORTH, SOUTH)
                '-' -> coordinate.move(WEST, EAST)
                'L' -> coordinate.move(NORTH, EAST)
                'J' -> coordinate.move(NORTH, WEST)
                '7' -> coordinate.move(SOUTH, WEST)
                'F' -> coordinate.move(SOUTH, EAST)
                else -> listOf()
            }.mapNotNull { get(it) }
        }

        fun findStart(): Node =
            rows.firstNotNullOf { row ->
                row.find { it.char == 'S' }
            }

        fun findMaxDistance(): Int =
            rows.maxOf { row ->
                row.maxOf { it.distance }
            }


        fun findNodesConnectedTo(coordinate: Coordinate): List<Node> =
            coordinate.allNeighbours().filter { c ->
                getNextNodes(c).map { it.coordinate }.contains(coordinate)
            }
                .mapNotNull { get(it) }

        fun findAll(filter: (Node) -> Boolean): List<Node> {
            return rows.flatMap { row -> row.filter(filter) }
        }
    }

    enum class HoleGroup {
        LEFT, RIGHT, NONE
    }

    data class Node(
        val char: Char,
        val coordinate: Coordinate,
        var distance: Int = -1,
        var holeGroup: HoleGroup = HoleGroup.NONE
    )

    data class Direction(
        val x: Int,
        val y: Int
    ) {
        companion object {
            val WEST = Direction(-1, 0)
            val EAST = Direction(1, 0)
            val NORTH = Direction(0, -1)
            val SOUTH = Direction(0, 1)
        }

        fun turnRight(): Direction {
            return Direction(-this.y, this.x)
        }

        fun turnLeft(): Direction {
            return Direction(this.y, -this.x)
        }
    }

    data class Coordinate(
        val x: Int,
        val y: Int
    ) {
        fun allNeighbours() = move(NORTH, SOUTH, EAST, WEST)

        fun move(other: Direction): Coordinate {
            return Coordinate(x + other.x, y + other.y)
        }

        fun directionFrom(other: Coordinate): Direction {
            return Direction(x - other.x, y - other.y)
        }

        fun move(vararg others: Direction): List<Coordinate> {
            return others.map { other -> Coordinate(x + other.x, y + other.y) }
        }
    }
}
