fun main() {

    val fileContent = AClass::class.java.getResource("1.txt")!!.readText()

    val sorted = fileContent.split("\n\n").map { s ->
        s.split("\n").sumOf { it.toInt() }
    }.sortedDescending()

    println(sorted.take(3).sum())
}


