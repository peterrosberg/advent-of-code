@file:OptIn(ExperimentalTime::class)

package com.pepp.adventofcode.y2022

import com.pepp.adventofcode.common.getFileContent
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main() {

    val fileContent = getFileContent("2022/16.txt")
    var valves = constructValves(fileContent)

    valves
        .forEach { valve ->

            val shortestPaths = valves
                .associate {
                    it.id to Integer.MAX_VALUE
                }.toMutableMap()

            calculateShortestPathToValve(shortestPaths, valve)

            val tunnelMap = shortestPaths
                .minus(valve.id)
                .mapNotNull {
                    val destination = valves.find { f -> f.id == it.key }!!
                    if (destination.flowRate > 0) Tunnel(it.value, destination) else null
                }.associateBy { it.to.id }

            valve.tunnels.clear()
            valve.tunnels.putAll(tunnelMap)
        }

    valves = valves.filter { it.flowRate > 0 || it.id == "AA" }

    printTunnels(valves)

    val start = valves.find { it.id == "AA" }!!

    val time = measureTime {
        val max = getMaxRelease(start, valves)
        println(max)
    }

    println("took $time")
}

private fun constructValves(fileContent: String): List<Valve2> {
    val valveRegex = "Valve (.+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)".toRegex()
    val valveData = fileContent
        .split("\n")
        .map { valveDesc ->
            println(valveDesc)
            val (id, flowRate, tunnels) = valveRegex.find(valveDesc)!!.destructured
            Triple(
                id,
                flowRate.toInt(),
                tunnels.split(", ")
            )
        }

    val v2s = valveData.map { v ->
        Valve2(v.first, v.second)
    }

    valveData.forEach { v ->
        val v2 = v2s.find { it.id == v.first }!!
        v.third.map { dest ->
            val tunnelTo = v2s.find { it.id == dest }!!
            Tunnel(1, tunnelTo)
        }.forEach { v2.tunnels[it.to.id] = it }
    }
    return v2s
}

fun getMaxRelease(start: Valve2, v2s: List<Valve2>, timeLeft: Int = 30): Int {

    if (timeLeft <= 0) return 0

    val subNet = v2s.minus(start)
    val openThisOne = start.flowRate * (timeLeft - 1) + getMaxFromTunnels(start, v2s, subNet, timeLeft - 1)

    val dontOpenThisOne = getMaxFromTunnels(start, v2s, subNet, timeLeft)

    return maxOf(openThisOne, dontOpenThisOne)
}

private fun getMaxFromTunnels(
    start: Valve2,
    v2s: List<Valve2>,
    subNet: List<Valve2>,
    timeLeft: Int
): Int = start.tunnels
    .filterValues { v2s.contains(it.to) }
    .map {
        getMaxRelease(it.value.to, subNet, timeLeft - it.value.distance)
    }
    .maxOrNull() ?: 0

fun calculateShortestPathToValve(shortestPaths: MutableMap<String, Int>, valve: Valve2, distance: Int = 0) {

    if (distance < shortestPaths[valve.id]!!) {
        shortestPaths[valve.id] = distance
        valve.tunnels.forEach {
            calculateShortestPathToValve(shortestPaths, it.value.to, distance + it.value.distance)
        }
    }
}

private fun printTunnels(v2s: List<Valve2>) {
    v2s.forEach { v ->
        println("Valve ${v.id} - flow rate: ${v.flowRate}")
        v.tunnels.forEach {
            println("  tunnel to ${it.key} - ${it.value.distance} (${it.value.to.flowRate})")
        }

    }
}

data class Valve2(
    val id: String,
    var flowRate: Int,
    val tunnels: MutableMap<String, Tunnel> = mutableMapOf(),
)

data class Tunnel(
    val distance: Int,
    val to: Valve2
)
