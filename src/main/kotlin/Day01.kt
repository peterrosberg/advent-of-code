fun main() {

    val fileContent = getFileContent("1.txt")

    val sorted = fileContent.split("\n\n").map { s ->
        s.split("\n").sumOf { it.toInt() }
    }.sortedDescending()

    println(sorted.take(3).sum())
}


