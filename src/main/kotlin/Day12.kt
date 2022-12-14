import common.Coordinate
import java.lang.Integer.min

fun main() {

    val fileContent = getFileContent("12.txt")

    var startingPoint: ElevationPoint? = null
    var endPoint: ElevationPoint? = null

    val map = fileContent.split("\n")
        .mapIndexed { x, line ->
            line.mapIndexed { y, c ->
                when (c) {
                    'S' -> {
                        startingPoint = ElevationPoint('a', Coordinate(x, y))
                        startingPoint!!
                    }
                    'E' -> {
                        endPoint = ElevationPoint('z', Coordinate(x, y))
                        endPoint!!
                    }
                    else -> ElevationPoint(c, Coordinate(x, y))
                }
            }
        }

    calculateDistances(map, endPoint!!)

    map.forEach { line ->
        line.forEach { print("${it.distance},") }
        println()
    }

    println(startingPoint!!.distance)

    val bestStartingPoint = map.flatMap { row ->
        row.filter { it.elevation == 'a' }
    }.minBy { it.distance }

    println(bestStartingPoint.distance)
}

fun calculateDistances(
    map: List<List<ElevationPoint>>, currentPoint: ElevationPoint, currentDistance: Int = 0,
) {

    if (currentPoint.distance <= currentDistance && currentPoint.visited) return

    currentPoint.distance = min(currentDistance, currentPoint.distance)
    currentPoint.visited = true

    Coordinate.ALL_DIRECTIONS.map {
        currentPoint.loc.plus(it)
    }
        .mapNotNull { getPoint(map, it) }
        .filter {
            (it.elevation + 1) >= currentPoint.elevation
        }
        .forEach { point ->
            calculateDistances(
                map,
                point,
                currentDistance + 1
            )
        }
}

fun getPoint(map: List<List<ElevationPoint>>, coordinate: Coordinate): ElevationPoint? =
    if (map.indices.contains(coordinate.x) && map[coordinate.x].indices.contains(coordinate.y))
        map[coordinate.x][coordinate.y]
    else null

data class ElevationPoint(
    val elevation: Char,
    val loc: Coordinate,
    var distance: Int = Int.MAX_VALUE,
    var visited: Boolean = false
)

