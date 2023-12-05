package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.common.getFileContent
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {

    val timeInMillis = measureTimeMillis {
        val fileContent = getFileContent("2023/5.txt")

        val sections = fileContent.split("\n\n")

        val seeds = sections.first().substringAfter(":").trim().split(" ").map { it.toLong() }
        val seedRanges = mutableListOf<LongRange>()
        for (i in seeds.indices step 2) {
            seedRanges.add(LongRange(seeds[i], seeds[i] + seeds[i + 1]))
        }

        val conversionData = sections.takeLast(sections.size - 1).map { section ->
            section.substringAfter(":\n")
                .trim()
                .split("\n")
                .map { it.split(" ").map(String::toLong) }
                .map { SeedMap(it) }
        }

        val result = conversionData.fold(seedRanges.toList()) { dataList, seedMaps ->
            //println(dataList)
            dataList.flatMap { seedRange ->
                convertSeedRange(seedRange, seedMaps)
            }
        }

        val part2 = result.fold(Long.MAX_VALUE) { curr, v -> min(curr, v.first) }
        println(part2)
    }
    println("Took $timeInMillis ms to run")
}

fun overlap(range1: LongRange, range2: LongRange): Boolean {
    return max(range1.first, range2.first) < min(range1.last, range2.last)
}

fun intersect(range1: LongRange, range2: LongRange): LongRange {
    return LongRange(max(range1.first, range2.first), min(range1.last, range2.last))
}

fun cutRange(original: LongRange, remove: LongRange): List<LongRange> {
    val out = mutableListOf<LongRange>()

    if (original.first < remove.first) out.add(LongRange(original.first, remove.first))
    if (original.last > remove.last) out.add(LongRange(remove.last, original.last))

    return out.toList()
}

fun convertSeedRange(seedRange: LongRange, seedMaps: List<SeedMap>): List<LongRange> {
    val out = mutableListOf<LongRange>()

    for (seedMap in seedMaps) {
        if (overlap(seedMap.sourceRange, seedRange)) {
            val coveredRange = intersect(seedRange, seedMap.sourceRange)
            out.add(seedMap.apply(coveredRange))

            cutRange(seedRange, coveredRange)
                .forEach { rest ->
                    out.addAll(convertSeedRange(rest, seedMaps))
                }
        }
    }

    return if (out.isEmpty()) listOf(seedRange)
    else out.toList()

}
