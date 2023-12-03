package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.getFileContent

fun main() {

    val fileContent = getFileContent("2022/20.txt")

    val inputList = fileContent.split("\n").mapIndexed { index, v ->
        EncryptedFileContent(v.toLong(), index)
    }

    val outputList = inputList.toMutableList()
    //mixFile(inputList, outputList)

    println("Mix 1: ${outputList.map { it.value }}")

    val answer = getAnswer(outputList)
    println("Answer 1: $answer")

    val decryptionKey = 811589153L
    val inputList2 = inputList.map {
        EncryptedFileContent(it.value * decryptionKey, it.originalPosition)
    }
    val outputList2 = inputList2.toMutableList()

    println("Init:  ${outputList2.map { it.value }}")
    for (i in 1..1) {
        mixFile(inputList2, outputList2)

        println("Mix $i: ${outputList2.map { it.value }}")
    }
    val answer2 = getAnswer(outputList2)
    println("Answer 2: $answer2")
}

private fun getAnswer(
    outputList: MutableList<EncryptedFileContent>
): Long {
    val indexOf0 = outputList.indexOfFirst { it.value == 0L }
    val answer = (1..3).map {
        outputList[(indexOf0 + it * 1000) % outputList.size].value
    }
    println(answer)
    return answer.sum()
}

private fun mixFile(
    inputList: List<EncryptedFileContent>,
    outputList: MutableList<EncryptedFileContent>
) {
    inputList.forEach { v ->
        val startPos = outputList.indexOf(v)
        val endPos = calculateNewPosition(startPos, v.value, inputList.size)

        outputList.removeAt(startPos)
        outputList.add(endPos, v)
        println(outputList.map { it.value })
    }
}

private fun calculateNewPosition(
    startPos: Int,
    v: Long,
    listSize: Int
) = if (v < 0) {
    val plusMove = listSize - 1 + (v / listSize + v.mod(listSize)).mod(listSize)
    val moving = startPos + plusMove
    (moving / listSize + moving.mod(listSize)).mod(listSize)
} else {
    val moving = startPos + v
    (moving / listSize + moving.mod(listSize)).mod(listSize)
}.toInt()

data class EncryptedFileContent(
    val value: Long,
    val originalPosition: Int
)