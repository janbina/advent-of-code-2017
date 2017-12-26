package Day22

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(22).readLines()

    val gridSize = 1000
    val grid = Array(gridSize) { CharArray(gridSize) { '.' } }
    val i = (gridSize - input.size) / 2
    val j = (gridSize - input[0].length) / 2

    val centerI = i + input.size/2
    val centerJ = j + input[0].length/2

    input.forEachIndexed { i2, line ->
        line.forEachIndexed { j2, c ->
            grid[i + i2][j + j2] = c
        }
    }

    assertEquals(5460, part1(grid.copy(), centerI to centerJ, -1 to 0))
    assertEquals(2511702, part2(grid.copy(), centerI to centerJ, -1 to 0))
}

fun part1(grid: Array<CharArray>, startPosition: Pair<Int, Int>, startDirection: Pair<Int, Int>): Int {
    var position = startPosition
    var direction = startDirection
    var burstsCausingInfection = 0

    repeat(10_000) {
        when (grid[position]) {
            '#' -> {
                grid[position] = '.'
                direction = direction.turnRight()
            }
            '.' -> {
                grid[position] = '#'
                direction = direction.turnLeft()
                burstsCausingInfection++
            }
        }
        position = position.move(direction)
    }

    return burstsCausingInfection
}

fun part2(grid: Array<CharArray>, startPosition: Pair<Int, Int>, startDirection: Pair<Int, Int>): Long {
    var position = startPosition
    var direction = startDirection
    var burstsCausingInfection = 0L

    repeat(10_000_000) {

        when (grid[position]) {
            '#' -> {
                grid[position] = 'F'
                direction = direction.turnRight()
            }
            'W' -> {
                grid[position] = '#'
                burstsCausingInfection++
            }
            'F' -> {
                grid[position] = '.'
                direction = direction.reverse()
            }
            '.' -> {
                grid[position] = 'W'
                direction = direction.turnLeft()
            }
        }
        position = position.move(direction)
    }

    return burstsCausingInfection
}

fun Pair<Int, Int>.turnRight() = second to -first
fun Pair<Int, Int>.turnLeft() = -second to first
fun Pair<Int, Int>.reverse() = -first to -second
fun Pair<Int, Int>.move(direction: Pair<Int, Int>) = (first + direction.first) to (second + direction.second)
fun Array<CharArray>.copy() = Array(size) { get(it).clone() }
operator fun Array<CharArray>.get(indices: Pair<Int, Int>) = this[indices.first][indices.second]
operator fun Array<CharArray>.set(indices: Pair<Int, Int>, value: Char) {
    this[indices.first][indices.second] = value
}