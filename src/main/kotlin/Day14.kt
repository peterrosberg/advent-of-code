import common.Coordinate
import kotlin.math.max
import kotlin.math.min

fun main() {

    val fileContent = getFileContent("14.txt")

    val cave = fileContent.split("\n")
        .map { path ->
            path.split(" -> ").map { coord ->
                Coordinate(
                    coord.split(",")[0].toInt(),
                    coord.split(",")[1].toInt()
                )
            }
        }.flatMap { path ->
            val rockPoints = mutableSetOf<Coordinate>()
            for (i in 1 until path.size) {
                val prev = path[i - 1]
                val curr = path[i]

                if (prev.x == curr.x) {
                    for (y in min(prev.y, curr.y)..max(prev.y, curr.y)) {
                        rockPoints.add(Coordinate(curr.x, y))
                    }
                } else {
                    for (x in min(prev.x, curr.x)..max(prev.x, curr.x)) {
                        rockPoints.add(Coordinate(x, curr.y))
                    }
                }
            }
            rockPoints.toList()
        }.plus(Coordinate(500, 0))


    println(cave)

    val maxX = cave.maxBy { it.x }.x
    val minX = cave.minBy { it.x }.x
    val maxY = cave.maxBy { it.y }.y
    val minY = cave.minBy { it.y }.y



    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (cave.contains(Coordinate(x, y))) {
                if (x == 500 && y == 0) print("+") else print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}

