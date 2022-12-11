fun main() {

    val fileContent = AClass::class.java.getResource("10.txt")!!.readText()

    var cycle = 1
    var x = 1

    val answer = fileContent.split("\n")
        .sumOf { cmd ->
            var signalStrength = registerCycle(cycle, x)
            if (cmd == "noop") {
                cycle++
            } else {
                val delta = cmd.split(" ")[1].toInt()
                cycle++
                signalStrength += registerCycle(cycle, x)
                cycle++
                x += delta
            }
            signalStrength
        }

    println(answer)

}

private fun registerCycle(cycle: Int, x: Int): Int {
    val pos = (cycle - 1) % 40
    val pixel = if (pos >= x - 1 && pos <= x + 1) {
        "#"
    } else {
        "."
    }
    print(pixel)
    if (cycle % 40 == 0) println()

    if (cycle % 40 == 20) {
        return cycle * x
    }
    return 0
}