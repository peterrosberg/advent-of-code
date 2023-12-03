package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.getFileContent

fun main() {

    val fileContent = getFileContent("2022/1.txt")

    val sorted = fileContent.split("\n\n").map { s ->
        s.split("\n").sumOf { it.toInt() }
    }.sortedDescending()

    println(sorted.take(3).sum())
}


