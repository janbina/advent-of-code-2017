package Day21

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(21)
            .readLines()
            .map { it.split(" => ").map { it.split('/').map { it.toCharArray() }.toTypedArray() } }
            .map { Enhancements.Enhancement(it[0], it[1]) }
            .let { Enhancements(it) }


    val initial = arrayOf(
            ".#.".toCharArray(),
            "..#".toCharArray(),
            "###".toCharArray()
    )

    assertEquals(167, generate(5, initial, input).countChars('#'))
    assertEquals(2425195, generate(18, initial, input).countChars('#'))
}

data class Enhancements(private val enhancements: List<Enhancement>) {

    data class Enhancement(val from: Array<CharArray>, val to: Array<CharArray>) {

        private val pixelsOn = from.countChars('#')

        fun matches(input: Array<CharArray>): Boolean {
            if (from.size != input.size) return false

            val size = from.size

            //center element stays always in place
            if (from.size == 3 && from[1][1] != input[1][1]) return false

            if (input.countChars('#') != pixelsOn) return false

            val flags = BooleanArray(8) { true }
            for (i in from.indices) {
                for (j in from.indices) {
                    //direct match
                    if (input[i][j] != from[i][j]) flags[0] = false
                    //horizontal flip
                    if (input[i][size-1-j] != from[i][j]) flags[1] = false
                    //vertical flip
                    if (input[size-1-i][j] != from[i][j]) flags[2] = false
                    //rotated 90
                    if (input[j][size-1-i] != from[i][j]) flags[3] = false
                    //rotated 180
                    if (input[size-1-i][size-1-j] != from[i][j]) flags[4] = false
                    //rotated 270
                    if (input[size-1-j][i] != from[i][j]) flags[5] = false
                    //rotated 90 and flipped vertically
                    if (input[size-1-j][size-1-i] != from[i][j]) flags[6] = false
                    //rotated 90 and flipped horizontally
                    if (input[j][i] != from[i][j]) flags[7] = false
                }
            }

            return flags.any { it }
        }
    }

    fun enhance(input: Array<CharArray>): Array<CharArray> {
        return enhancements
                .first { it.matches(input) }
                .to
    }
}

fun generate(iterations: Int, initial: Array<CharArray>, enhancements: Enhancements): Array<CharArray> {
    var current = initial

    repeat(iterations) {
        val size = current.size
        val partSize = if (size % 2 == 0) 2 else 3
        val parts = size / partSize
        val nextSize = parts * (partSize + 1)
        val next = Array(nextSize) { CharArray(nextSize) }

        for (i in 0 until parts) {
            for (j in 0 until parts) {
                val square = current.getSquare(i * partSize, j * partSize, partSize)
                val enhanced = enhancements.enhance(square)
                next.insertSquare(enhanced, i * (partSize + 1), j * (partSize + 1))
            }
        }

        current = next
    }

    return current
}

fun Array<CharArray>.getSquare(i: Int, j: Int, size: Int): Array<CharArray> {
    val new = Array(size) { CharArray(size) }

    for (ix in i until i + size) {
        for (jx in j until j + size) {
            new[ix - i][jx - j] = this[ix][jx]
        }
    }

    return new
}

fun Array<CharArray>.insertSquare(square: Array<CharArray>, i: Int, j: Int) {
    for (ix in i until i + square.size) {
        for (jx in j until j + square.size) {
            this[ix][jx] = square[ix - i][jx - j]
        }
    }
}

fun Array<CharArray>.countChars(c: Char): Int {
    return this.sumBy { it.count { it == c } }
}