package com.pepp.adventofcode.y2023

import com.pepp.adventofcode.y2023.Day10.Direction.Companion.EAST
import com.pepp.adventofcode.y2023.Day10.Direction.Companion.NORTH
import com.pepp.adventofcode.y2023.Day10.Direction.Companion.SOUTH
import com.pepp.adventofcode.y2023.Day10.Direction.Companion.WEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day10Test {

    @Test
    fun `turn direction right`() {

        assertThat(EAST.turnLeft()).isEqualTo(NORTH)
        assertThat(NORTH.turnLeft()).isEqualTo(WEST)
        assertThat(WEST.turnLeft()).isEqualTo(SOUTH)
        assertThat(SOUTH.turnLeft()).isEqualTo(EAST)

    }

    @Test
    fun `turn direction left`() {

        assertThat(EAST.turnRight()).isEqualTo(SOUTH)
        assertThat(NORTH.turnRight()).isEqualTo(EAST)
        assertThat(WEST.turnRight()).isEqualTo(NORTH)
        assertThat(SOUTH.turnRight()).isEqualTo(WEST)

    }

}