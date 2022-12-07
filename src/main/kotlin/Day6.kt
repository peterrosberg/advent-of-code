fun main() {

    val fileContent = AClass::class.java.getResource("6.txt")!!.readText()

    val marker = Marker(14)
    for (i in fileContent.indices) {
        marker.addChar(fileContent[i])
        if (marker.isMarker()) {
            println("Got to ${i+1} and the marker was $marker")
            break
        }
    }
}

class Marker(
    val size: Int
) {
    private val last4 = mutableListOf<Char>()

    fun addChar(c: Char) {
        last4.add(c)
        if(last4.size > size) last4.removeFirst()
    }

    fun isMarker(): Boolean = last4.toSet().size == size

    override fun toString(): String {
        return last4.joinToString()
    }
}
