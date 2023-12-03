package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.getFileContent

fun main() {

    val fileContent = getFileContent("2022/5.txt")
    val isPartB = true

    val crateStack = "\\[([A-Z])]| {4}".toRegex()
    val move = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

    val stacks = IntRange(0, 8).map {
        mutableListOf<String>()
    }.toMutableList()

    fileContent.split("\n")
        .forEach { row ->
            if (crateStack.containsMatchIn(row)) {
                buildStack(crateStack, row, stacks)
            } else {
                val result = move.find(row)
                if (result != null) {
                    val (nr, from, to) = result.destructured

                    val source = stacks[from.toInt() - 1]
                    val destination = stacks[to.toInt() - 1]

                    val liftedCrates =
                        if (isPartB) source.takeLast(nr.toInt()) else source.takeLast(nr.toInt()).reversed()
                    destination.addAll(liftedCrates)
                    for (count in 0 until nr.toInt()) {
                        source.removeLastOrNull()
                    }
                }
            }
            println(stacks)
        }

    println(stacks)

    val result = stacks.map { it.last() }.fold("") { acc, s -> "$acc$s" }
    println(result)
}

private fun buildStack(
    crateStack: Regex,
    row: String,
    stacks: MutableList<MutableList<String>>
) {
    crateStack.findAll(row).forEachIndexed { index, value ->
        val crate = value.groupValues[1]
        if (crate != "") {
            println("$index ${value.groupValues}")
            stacks[index].add(0, crate)
        }
    }
}