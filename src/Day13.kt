package day13

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(13)
            .readLines()
            .map { it.split(": ").map { it.toInt() } }
            .map { Layer(it[0], it[1]) }

    assertEquals(1624, part1(input))
    assertEquals(3923436, part2(input))
}

data class Layer(val depth: Int, val range: Int) {
    val severity = depth * range

    fun caught(start: Int): Boolean {
        val enterTime = start + depth

        return (enterTime % (2 * (range - 1))) == 0
    }
}

fun part1(input: List<Layer>): Int {
    return input.filter { it.caught(0) }.sumBy { it.severity }
}

fun part2(input: List<Layer>): Int {
    var delay = 0

    while (true) {
        input.all { !it.caught(delay) }.let {
            if (it) return delay
        }
        delay++
    }
}
