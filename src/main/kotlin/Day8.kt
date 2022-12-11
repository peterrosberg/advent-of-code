import common.Coordinate

val directions = listOf(
    Coordinate(0, 1),
    Coordinate(0, -1),
    Coordinate(1, 0),
    Coordinate(-1, 0)
)

fun main() {

    val fileContent = AClass::class.java.getResource("8.txt")!!.readText()
    val grid = fileContent.split("\n")
        .map { row ->
            row.map { treeHeight ->
                Tree(treeHeight.digitToInt())
            }
        }



    for (x in grid.indices) {
        loopThroughLineOfSight(grid, Coordinate(x, 0), Coordinate(0, 1))
        loopThroughLineOfSight(grid, Coordinate(x, grid[0].size - 1), Coordinate(0, -1))
    }

    for (y in grid[0].indices) {
        loopThroughLineOfSight(grid, Coordinate(0, y), Coordinate(1, 0))
        loopThroughLineOfSight(grid, Coordinate(grid.size - 1, y), Coordinate(-1, 0))
    }

    val visibleTrees = grid.sumOf { row ->
        row.filter { it.visible }.size
    }
    println(visibleTrees)

    val maxScenicScore = grid.mapIndexed { x, row ->
        List(row.size) { y -> calculateScenicScore(grid, Coordinate(x, y)) }.max()
    }.max()

    println(maxScenicScore)
}

private fun loopThroughLineOfSight(
    grid: List<List<Tree>>,
    startLocation: Coordinate,
    direction: Coordinate
) {
    var location = startLocation
    var currentMaxHeight = Int.MIN_VALUE

    var currentTree = getTree(grid, location)
    while (currentTree != null) {
        if (currentTree.height > currentMaxHeight) {
            currentMaxHeight = currentTree.height
            currentTree.visible = true
        }
        location = location.plus(direction)
        currentTree = getTree(grid, location)
    }
}

private fun calculateScenicScore(grid: List<List<Tree>>, location: Coordinate): Int {


    val tree1 = getTree(grid, location)!!
    val treeHeight = tree1.height

    val totalScore = directions.map { d ->
        var l = location
        var score = 0
        do {
            l = l.plus(d)
            val tree = getTree(grid, l)
            if (tree != null) score++
        } while (tree != null && tree.height < treeHeight)
        score
    }.fold(1) { it, acc -> it * acc }
    if (totalScore > 100000) println("$location, $tree1 $totalScore")
    return totalScore

}

private fun getTree(
    grid: List<List<Tree>>,
    location: Coordinate
) = if (!grid.indices.contains(location.x) || !grid[location.x].indices.contains(location.y))
    null else grid[location.x][location.y]

data class Tree(
    val height: Int,
    var visible: Boolean = false
)

