package day05

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(5).readLines().map { it.toInt() }

    assertEquals(374269, part1(input))
    assertEquals(27720699, part2(input))
}

fun part1(input: List<Int>): Int {
    return countSteps(input, { it.inc() })
}

fun part2(input: List<Int>): Int {
    return countSteps(input, { if (it >= 3) it.dec() else it.inc() })
}

fun countSteps(instructions: List<Int>, instructionTransformation: (Int) -> Int): Int {
    val inst = instructions.toMutableList()
    var pc = 0
    var steps = 0

    while (true) {
        if (pc !in inst.indices) {
            return steps
        }
        inst[pc] = inst[pc].let {
            pc += it
            instructionTransformation(it)
        }
        steps++
    }
}