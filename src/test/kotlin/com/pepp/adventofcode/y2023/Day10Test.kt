package com.pepp.adventofcode.y2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day10Test {

    @Test
    fun `turn direction right`() {

        assertThat(Day10.EAST.turnLeft()).isEqualTo(Day10.NORTH)
        assertThat(Day10.NORTH.turnLeft()).isEqualTo(Day10.WEST)
        assertThat(Day10.WEST.turnLeft()).isEqualTo(Day10.SOUTH)
        assertThat(Day10.SOUTH.turnLeft()).isEqualTo(Day10.EAST)

    }

    @Test
    fun `turn direction left`() {

        assertThat(Day10.EAST.turnRight()).isEqualTo(Day10.SOUTH)
        assertThat(Day10.NORTH.turnRight()).isEqualTo(Day10.EAST)
        assertThat(Day10.WEST.turnRight()).isEqualTo(Day10.NORTH)
        assertThat(Day10.SOUTH.turnRight()).isEqualTo(Day10.WEST)

    }

}