package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day08.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day08 {
    val pattern = "(\\w+) = \\((\\w+), (\\w+)\\)".toRegex()

    fun main() {
        val fileContent = getFileContent("2023/8.txt")

        val (instructions, nodes) = fileContent.split("\n\n")

        val nodeMap = nodes.split("\n").associate { line ->
            val (_, node, left, right) = pattern.find(line)!!.groupValues
            node to Pair(left, right)
        }

        part2(nodeMap, instructions)

    }

    private fun part1(
        nodeMap: Map<String, Pair<String, String>>,
        instructions: String
    ) {
        var currentNode = "AAA"
        var stepCount = 0
        while (currentNode != "ZZZ") {
            val choice = nodeMap[currentNode]!!
            val instruction = instructions[stepCount.mod(instructions.length)]
            currentNode = if (instruction == 'L') choice.first
            else choice.second
            stepCount++
        }
        println(currentNode)
        println(stepCount)
    }

    private fun part2(
        rawMap: Map<String, Pair<String, String>>,
        instructions: String
    ) {
        val nodesMap = rawMap.mapValues { startNode ->
            mapNode(startNode.key, rawMap, instructions)
        }

        val startNodes = nodesMap.keys.filter { it.last() == 'A' }
            .map { nodesMap[it]!! }
        println(startNodes.size)

        val distances = nodesMap.keys.filter { it.last() == 'A' }.map { singlePart2(it, rawMap, instructions) }

        //They are all full iterations!!
        val commondivisor = distances.map { it / instructions.length }.fold(1L) { a, b -> a * b }

        println(commondivisor * instructions.length)
    }

    private fun singlePart2(
        startNode: String,
        nodeMap: Map<String, Pair<String, String>>,
        instructions: String
    ): Int {
        var currentNode = startNode
        var stepCount = 0
        while (currentNode.last() != 'Z') {
            val choice = nodeMap[currentNode]!!
            val instruction = instructions[stepCount.mod(instructions.length)]
            currentNode = if (instruction == 'L') choice.first
            else choice.second
            stepCount++
        }
        return stepCount
    }

    private fun mapNode(
        startNode: String,
        nodeMap: Map<String, Pair<String, String>>,
        instructions: String
    ): Node {
        var currentNode = startNode
        val endNodes = mutableListOf<Int>()
        instructions.forEachIndexed { index, instruction ->
            if (currentNode.last() == 'Z') endNodes.add(index)
            val choice = nodeMap[currentNode]!!
            currentNode = if (instruction == 'L') choice.first
            else choice.second
        }

        return Node(currentNode, endNodes.toList())
    }

    data class Node(
        val fullIterationLeadsTo: String,
        val endNodesAt: List<Int>
    )
}
