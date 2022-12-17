package com.pepp.adventofcode.common

class Grid<T>(
    private val map: List<List<T>>
) : Collection<T> {

    override val size: Int = map.sumOf { it.size }

    fun getItem(x: Int, y: Int): T? =
        if (map.indices.contains(x) && map[x].indices.contains(y))
            map[x][y]
        else null

    fun getItem(coordinate: Coordinate) = getItem(coordinate.x, coordinate.y)

    fun forEachRow(func: (List<T>) -> Unit) = map.forEach(func)

    fun filter(func: (T) -> Boolean): List<T> = map.flatMap { it.filter(func) }


    override fun isEmpty(): Boolean {
        @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
        return size == 0
    }

    override fun iterator(): Iterator<T> = GridIterator(this)

    override fun containsAll(elements: Collection<T>): Boolean = elements.all { contains(it) }

    override fun contains(element: T): Boolean = map.any { it.contains(element) }

    class GridIterator<T>(
        grid: Grid<T>
    ) : Iterator<T> {

        private val items = grid.map.flatten().iterator()

        override fun hasNext(): Boolean = items.hasNext()

        override fun next(): T = items.next()
    }
}


fun <T> List<List<T>>.toGrid(): Grid<T> {
    return Grid(this.toList())
}