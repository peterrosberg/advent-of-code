package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.math.min

fun main() {

    val fileContent = getFileContent("2023/5.txt")

    val sections = fileContent.split("\n\n")

    val seeds = sections.first().substringAfter(":").trim().split(" ").map { it.toLong() }

    println(seeds)

    val conversionData = sections.takeLast(sections.size - 1).map { section ->
        section.substringAfter(":\n")
            .trim()
            .split("\n")
            .map { it.split(" ").map(String::toLong) }
            .map { SeedMap(it) }
    }

    val result = conversionData.fold(seeds) { dataList, seedMaps ->
        convert(dataList, seedMaps)
    }

    println(result)

    val part1 = result.fold(Long.MAX_VALUE) { curr, v -> min(curr, v) }
    println(part1)
}

fun convert(seeds: List<Long>, seedMaps: List<SeedMap>): List<Long> {
    val conversions = mutableMapOf<Long, Long>()

    seedMaps.forEach { map ->
        seeds
            .filter { !conversions.contains(it) }
            .forEach { seed ->
                if (map.contains(seed)) {
                    conversions[seed] = map.apply(seed)
                }
            }
    }

    return seeds.map { seed ->
        val output = conversions.getOrDefault(seed, seed)
        //println("$seed -> $output")
        output
    }

}

class SeedMap(input: List<Long>) {
    val sourceRange: LongRange
    val delta: Long

    init {
        val (destStart, sourceStart, rangeLength) = input
        sourceRange = LongRange(sourceStart, sourceStart + rangeLength)
        delta = destStart - sourceStart
    }

    fun contains(seed: Long) = sourceRange.contains(seed)
    fun apply(seed: Long) = seed + delta
    fun apply(range: LongRange) = LongRange(range.first + delta, range.last + delta)
}
