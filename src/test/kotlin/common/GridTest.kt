package common

import com.pepp.adventofcode.common.Grid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GridTest {

    @Test
    fun `should return correct size`() {
        val grid = Grid(listOf(listOf("1", "2"), listOf("2", "3")))

        assertThat(grid.size).isEqualTo(4)
    }

}