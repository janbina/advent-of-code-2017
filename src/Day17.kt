package day17

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(17).readLines().first().toInt()

    assertEquals(640, part1(input))
    assertEquals(47949463, part2(input))
}

fun part1(input: Int): Int {
    val buffer = mutableListOf(0)
    var position = 0

    (1..2017).forEach {
        position = (position + input) % buffer.size + 1
        buffer.add(position, it)
    }

    return buffer[position + 1]
}

fun part2(input: Int): Int {
    var zeroPos = 0
    var currentPos = 0
    var afterZeroValue = -1
    var bufferSize = 1

    (1..50_000_000).forEach {
        currentPos = (currentPos + input) % bufferSize + 1
        if (currentPos <= zeroPos) {
            zeroPos++
        } else if (currentPos == zeroPos + 1) {
            afterZeroValue = it
        }
        bufferSize++
    }

    return afterZeroValue
}
