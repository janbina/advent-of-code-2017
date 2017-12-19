package day19

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(19).readLines()
    val max = input.map { it.length }.max() ?: 0
    val paddedInput = input.map { it.padEnd(max + 1, ' ') }

    val result = tracePacket(paddedInput)

    assertEquals("SXWAIBUZY", result.first)
    assertEquals(16676, result.second)
}

fun tracePacket(input: List<String>): Pair<String, Int> {
    val startI = 0
    val startJ = input[0].withIndex().first { it.value == '|' }.index
    var position = startI to startJ
    var direction = 1 to 0
    var letters = ""
    var steps = 0

    while (position.first in input.indices && position.second in input[position.first].indices) {
        val current = input[position.first][position.second]
        if (current == '+') {
            if (direction.first == 0) {
                if (input[position.first + 1][position.second] != ' ') {
                    direction = 1 to 0
                } else if (input[position.first - 1][position.second] != ' ') {
                    direction = -1 to 0
                }
            } else if (direction.second == 0) {
                if (input[position.first][position.second + 1] != ' ') {
                    direction = 0 to 1
                } else if (input[position.first][position.second - 1] != ' ') {
                    direction = 0 to -1
                }
            }
        } else if (current.isLetter()) {
            letters += current
        } else if (current.isWhitespace()) {
            break
        }

        steps++
        position += direction
    }

    return letters to steps
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (this.first + other.first) to (this.second + other.second)
}