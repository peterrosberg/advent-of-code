fun main() {

    val fileContent = AClass::class.java.getResource("2.txt")!!.readText()

    val sum = fileContent.split("\n")
        .map { s ->
            val split = s.split(" ")
            Pair(split[0], split[1])
        }.sumOf { calculateScore(it) }

    println(sum)
}

fun calculateScore(p: Pair<String, String>): Int {
    return when (p.second) {
        "X" -> { //Lose
            0 + when (p.first) {
                "A" -> { //Rock -> Scissors
                    3
                }
                "B" -> { //Paper
                    1
                }
                else -> { //scissors
                    2
                }
            }
        }
        "Y" -> { //draw
            3 + when (p.first) {
                "A" -> { //Rock
                    1
                }
                "B" -> { //Paper
                    2
                }
                else -> { //scissors
                    3
                }
            }
        }
        else -> { //win
            6 + when (p.first) {
                "A" -> { //Rock
                    2
                }
                "B" -> { //Paper
                    3
                }
                else -> { //scissors
                    1
                }
            }
        }
    }
}


