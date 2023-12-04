package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.math.max

fun main() {

    val fileContent = getFileContent("2023/4.txt")

    val cardMatches = fileContent.split("\n")
        .map { it.substringAfter(":") }
        .map { row ->
            val (winning, yours) = row.split("|")
                .map { numbers ->
                    numbers.trim()
                        .split(" ")
                        .filter { it.isNotEmpty() }
                }
            yours.count { winning.contains(it) }
        }

    val part1 = cardMatches
        .sumOf { matches ->
            max(1 shl (matches - 1), 0)
        }
    println(part1)

    val part2 = (0..cardMatches.size).sumOf {
        processScratchCard(cardMatches.subList(it, cardMatches.size))
    }
    println(part2)
}

fun processScratchCard(cardMatches: List<Int>): Int {

    if (cardMatches.isEmpty()) return 0

    return if (cardMatches[0] > 0) {
        1 + (1..cardMatches[0]).sumOf {
            processScratchCard(cardMatches.subList(it, cardMatches.size))
        }
    } else {
        1
    }
}