package day03

import getInput
import kotlin.coroutines.experimental.buildSequence
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.test.assertEquals

enum class Direction { TOP, LEFT, BOTTOM, RIGHT }

fun main(args: Array<String>) {
    val input = getInput(3).readLines().first().toInt()

    assertEquals(430, part1(input))
    assertEquals(312453, part2(input))
}

fun spiral(): Sequence<Pair<Int, Int>> = buildSequence {
    var x = 0
    var y = 0
    var dx = 1
    var dy = 0

    while (true) {
        yield(Pair(x, y))
        if ((abs(x) == abs(y) && (x < 0 || y < 0)) || (y >= 0 && (x - y) == 1)) {
            val tmp = dx
            dx = dy
            dy = -tmp
        }
        x += dx
        y += dy
    }
}

fun part1(input: Int): Int {
    return spiral().take(input).last().run { abs(first) + abs(second) }
}

fun part2(input: Int): Int {
    val gridSize = ceil(sqrt(input.toDouble())).toInt()
    val grid = Array(gridSize) { IntArray(gridSize, { 0 }) }

    var i = gridSize / 2
    var j = gridSize / 2
    grid[i][j] = 1
    grid[i--][++j] = 1

    var prevValue = 1
    var direction = Direction.TOP

    while (true) {
        val nextValue = (grid[i][j-1] + grid[i][j+1] + grid[i-1][j] + grid[i+1][j]
                + grid [i-1][j-1] + grid [i-1][j+1] + grid [i+1][j+1] + grid [i+1][j-1])
        if (nextValue == prevValue) {
            //if next value is same as previous, we are too far in this direction
            //return one step back and change direction
            direction = when (direction) {
                Direction.TOP -> { i++; j--; Direction.LEFT }
                Direction.LEFT -> { i++; j++; Direction.BOTTOM }
                Direction.BOTTOM -> { i--; j++; Direction.RIGHT }
                Direction.RIGHT -> { i--; j--; Direction.TOP }
            }
            continue
        }
        if (nextValue > input) {
            return nextValue
        }
        prevValue = nextValue
        grid[i][j] = nextValue
        when (direction) {
            Direction.TOP -> i--
            Direction.LEFT -> j--
            Direction.BOTTOM -> i++
            Direction.RIGHT -> j++
        }
    }
}