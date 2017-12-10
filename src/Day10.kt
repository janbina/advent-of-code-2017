package day10

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(10).readLines().first()

    assertEquals(52070, part1(input))
    assertEquals("7f94112db4e32e19cf6502073c66f9bb", part2(input))
}

fun part1(input: String): Int {
    val lengths = input.split(",").map { it.toInt() }

    val hash = knotHash(lengths, 1)

    return hash[0] * hash[1]
}


fun part2(input: String): String {
    val lengths = input.map { it -> it.toInt() } + listOf(17, 31, 73, 47, 23)

    val hash = knotHash(lengths)

    return hash.withIndex()
            .groupingBy { it.index / 16 }
            .fold(0, { acc, next -> acc xor next.value })
            .toSortedMap()
            .values.joinToString(separator = "") { it.toString(16).padStart(2, '0') }
}

fun knotHash(lengths: List<Int>, rounds: Int = 64): IntArray {
    val list = IntArray(256, { i -> i })
    var currentPosition = 0
    var skipSize = 0

    repeat(rounds) {
        lengths.forEach { length ->
            list.reverse(currentPosition, length)
            currentPosition += length + skipSize
            currentPosition %= list.size
            skipSize++
        }
    }

    return list
}

fun IntArray.reverse(start: Int, length: Int) {
    if (length > this.size) {
        throw IllegalArgumentException()
    }

    var startPointer = start % this.size
    var endPointer = (start + length - 1) % this.size

    repeat(length / 2) {
        val tmp = this[startPointer]
        this[startPointer] = this[endPointer]
        this[endPointer] = tmp
        startPointer++
        endPointer--
        if (startPointer >= this.size) startPointer = 0
        if (endPointer < 0) endPointer = this.size - 1
    }
}

