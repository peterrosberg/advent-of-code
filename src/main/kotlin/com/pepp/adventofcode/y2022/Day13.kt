package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.getFileContent

fun main() {

    val fileContent = getFileContent("2022/13.txt")

    val partA = fileContent.split("\n\n")
        .map { pair ->
            val first = parseRecursiveList(pair.split("\n")[0])
            val second = parseRecursiveList(pair.split("\n")[1])

            Day13Comparator().compare(first, second)
        }.mapIndexed { index, i ->
            if (i <= 0) index + 1 else 0
        }.sum()


    println(partA)

    val partB = fileContent.split("\n\n", "\n")
        .plus(listOf("[[2]]", "[[6]]"))
        .map {
            parseRecursiveList(it)
        }.sortedWith(Day13Comparator())


    partB.forEach { println(it) }

    val firstMarkerIndex = partB.indexOf(listOf(listOf(2))) + 1
    val secondMarkerIndex = partB.indexOf(listOf(listOf(6))) + 1
    println(firstMarkerIndex * secondMarkerIndex)
}

fun parseRecursiveList(input: String): Any {

    if (!input.startsWith("[")) return input.toInt()

    val parsingText = input.removePrefix("[").removeSuffix("]")
    if (parsingText.isEmpty()) return listOf<Any>()
    var pCount = 0;
    var startOfElement = 0
    val outputList = mutableListOf<Any>()

    for (i in parsingText.indices) {
        if (parsingText[i] == '[') {
            pCount++
        } else if (parsingText[i] == ']') {
            pCount--
        } else if (parsingText[i] == ',') {
            if (pCount == 0) {
                outputList.add(parseRecursiveList(parsingText.substring(startOfElement, i)))
                startOfElement = i + 1
            }
        }
    }
    outputList.add(parseRecursiveList(parsingText.substring(startOfElement, parsingText.length)))

    return outputList
}

class Day13Comparator : Comparator<Any> {
    override fun compare(first: Any?, second: Any?): Int {
        if (first == null && second == null) return 0
        if (first == null) return -1
        if (second == null) return 1

        if (first is Int && second is Int) {
            return first.toInt().compareTo(second.toInt())
        }

        val firstList = if (first is List<*>) first else listOf(first)
        val secondList = if (second is List<*>) second else listOf(second)

        for (i in 0 until kotlin.math.max(firstList.size, secondList.size)) {
            if (i >= firstList.size) return -1
            if (i >= secondList.size) return 1

            val recurse = compare(firstList[i], secondList[i])
            if (recurse != 0) return recurse
        }

        return 0
    }
}
