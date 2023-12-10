package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        Day07.main()
    }
    println("Took $timeInMillis ms to run")
}

object Day07 {
    const val VALUES = "AKQJT98765432"

    fun main() {
        val fileContent = getFileContent("2023/7.txt")

        val hands = fileContent.split("\n").map { Hand(it) }

        val sortedHands = hands.sortedWith(HandComparator())

        val winnings = sortedHands.mapIndexed { index, hand ->
            (index + 1) * hand.bid
        }.sum()

        println(winnings)
    }

    class HandComparator : Comparator<Hand> {
        override fun compare(o1: Hand?, o2: Hand?): Int {
            if (o1 == null || o2 == null) return 0

            if (o1.type == o2.type) {
                for (i in o1.hand.indices) {
                    val o1Value = VALUES.indexOf(o1.hand[i])
                    val o2Value = VALUES.indexOf(o2.hand[i])
                    if (o1Value != o2Value) {
                        return o2Value - o1Value
                    }
                }
                return 0
            }

            return o1.type - o2.type
        }
    }

    class Hand(input: String) {

        val hand: String
        val bid: Int
        val type: Int

        init {
            val (hand1, bid1) = input.split(" ")
            hand = hand1
            bid = bid1.toInt()

            type = determineType(hand)
        }

        private fun determineType(hand: String): Int {
            val groupedByValue = hand.groupBy { it }
            val groupOfFive = groupedByValue.any { it.value.size == 5 }
            val groupOfFour = groupedByValue.any { it.value.size == 4 }
            val groupOfThree = groupedByValue.any { it.value.size == 3 }
            val groupsOfTwo = groupedByValue.filter { it.value.size == 2 }.count()

            if (groupOfFive) return 7
            if (groupOfFour) return 6
            if (groupOfThree && groupsOfTwo == 1) return 5
            if (groupOfThree) return 4
            if (groupsOfTwo == 2) return 3
            if (groupsOfTwo == 1) return 2
            return 1
        }
    }
}
