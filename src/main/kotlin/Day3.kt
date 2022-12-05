fun main(args: Array<String>) {

    val fileContent = AClass::class.java.getResource("3.txt")!!.readText()

    val rows = fileContent.split("\n")
    val sum = rows
        .map { s ->
            val first = s.substring(0 until s.length/2)
            val second = s.substring(s.length/2)

            first.find { second.contains(it) }!!
        }.sumOf { calculatePrio(it) }

    println(sum)

    var sum2 = 0
    for (i in rows.indices step 3) {
        val group = rows.slice(i+1..i+2)
        val char = rows[i].find {s ->
            group.all { it.contains(s) }
        }!!
        sum2 += calculatePrio(char)
    }
    println(sum2)

}

fun calculatePrio(char: Char): Int = if (char.isUpperCase()) {
    char.minus('A')+27
} else {
    char.minus('a')+1
}
