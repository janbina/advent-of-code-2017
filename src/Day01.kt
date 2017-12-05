package day01

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(1).readLines().first()

    assertEquals(1177, part1(input))
    assertEquals(1060, part2(input))
}

fun part1(input: String): Int {
    return (input + input.first())
            .zipWithNext()
            .filter { it.first == it.second }
            .sumBy { it.first.toString().toInt() }
}

fun part2(input: String): Int {
    return input
            .zip(input.substring(input.length / 2) + input)
            .filter { it.first == it.second }
            .sumBy { it.first.toString().toInt() }
}