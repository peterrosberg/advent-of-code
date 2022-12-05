fun main() {

    val fileContent = AClass::class.java.getResource("4.txt")!!.readText()

    val pairRanges = fileContent.split("\n")
        .mapNotNull { line ->
            "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex().matchEntire(line)?.groupValues
        }
        .map { groups ->
            groups.slice(1..4).map { it.toInt() }
        }

    val sumOfCompleteOverlaps = pairRanges
        .filter {
            ((it[0] <= it[2] && it[1] >= it[3])
                    || (it[0] >= it[2] && it[1] <= it[3]))

        }.size

    println(sumOfCompleteOverlaps)

    val sumOfAnyOverlaps = pairRanges
        .filter {
            (it[0] <= it[2] && it[1] >= it[2])
                    || (it[0] <= it[3] && it[1] >= it[3])
                    || (it[0] <= it[2] && it[1] >= it[3])
                    || (it[0] >= it[2] && it[1] <= it[3])
        }.size

    println(sumOfAnyOverlaps)
}