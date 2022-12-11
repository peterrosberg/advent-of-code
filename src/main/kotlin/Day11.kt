fun main() {
    val monkeyPattern =
        "Starting items: (.+)\n\\s+Operation: new = (.+)\n\\s+Test: divisible by (\\d+)\n\\s+If true: throw to monkey (\\d+)\n\\s+If false: throw to monkey (\\d+)".toRegex()

    val fileContent = AClass::class.java.getResource("11.txt")!!.readText()

    val monkeys = fileContent.split("\n\n")
        .map { monkeyDescription ->
            val (items, operation, divider, monkeyIfTrue, monkeyIfFalse) = monkeyPattern.find(monkeyDescription)!!.destructured
            val itemsParsed = items.split(", ")
                .map { it.toLong() }
                .toMutableList()
            Monkey(
                itemsParsed,
                parseOperation(operation),
                divider.toLong(),
                monkeyIfTrue.toInt(),
                monkeyIfFalse.toInt()
            )
        }

    val maxDivider = monkeys.map { it.testValue }.fold(1L) { acc, v -> acc * v }

    for (round in 1..10_000) {

        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                monkey.inspectionCounter++
                val newItemValue = monkey.operation(item) % maxDivider
                val nextMonkey = monkey.getNextMonkey(newItemValue)
                monkeys[nextMonkey].items.add(newItemValue)
            }
            monkey.items.clear()
        }

        if (round % 1000 == 0 || round < 20) {
            println("== After round $round ==")
            printInspectionRates(monkeys)
        }
    }

    val sortedMonkeyBusiness = monkeys.map { it.inspectionCounter }.sortedDescending()
    val result = sortedMonkeyBusiness[0] * sortedMonkeyBusiness[1]

    println("\nAnswer: $result")

}

private fun printInspectionRates(monkeys: List<Monkey>) {
    monkeys.forEachIndexed { index, monkey ->
        println("Monkey $index: inspected items ${monkey.inspectionCounter} times")
    }
}

data class Monkey(
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val testValue: Long,
    private val monkeyIfTrue: Int,
    private val monkeyIfFalse: Int,
    var inspectionCounter: Long = 0L
) {
    fun getNextMonkey(item: Long): Int = if (item % testValue == 0L) monkeyIfTrue else monkeyIfFalse
}

fun parseOperation(op: String): (Long) -> Long {
    val (operator, arg2) = ".+ ([+*]) (.+)".toRegex().matchEntire(op)!!.destructured
    val operation: (Long, Long) -> Long =
        if (operator == "+") { p1, p2 -> p1 + p2 }
        else { p1, p2 -> p1 * p2 }

    return if (arg2 != "old") {
        { old -> operation(old, arg2.toLong()) }
    } else {
        { old -> operation(old, old) }
    }
}
