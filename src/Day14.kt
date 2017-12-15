package day14

import getInput
import java.util.*
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(14).readLines().first()

    assertEquals(8194, part1(input))
    assertEquals(1141, part2(input))
}

fun part1(input: String): Int {
    return makeMatrix(input).map {
        it.count { it }
    }.sum()
}

fun part2(input: String): Int {
    var groups = 0
    val queue = LinkedList<Pair<Int, Int>>()
    val matrix = makeMatrix(input)

    for (i in 0..127) {
        for (j in 0..127) {
            if (matrix[i][j]) {
                queue.add(i to j)
                groups++
                while (queue.isNotEmpty()) {
                    val current = queue.pop()
                    if (current.first in 0..127 &&
                            current.second in 0..127 &&
                            matrix[current.first][current.second]) {
                        matrix[current.first][current.second] = false
                        queue.add(current.first-1 to current.second)
                        queue.add(current.first+1 to current.second)
                        queue.add(current.first to current.second-1)
                        queue.add(current.first to current.second+1)
                    }
                }
            }
        }
    }

    return groups
}

fun makeMatrix(input: String): Array<BooleanArray> {
    val matrix = Array(128) { BooleanArray(128) }
    (0..127).map { i ->
        val hash = day10.part2("$input-$i")
        var j = 0
        hash.forEach {
            val num = it.toString().toInt(16)
            matrix[i][j++] = num and 8 > 0
            matrix[i][j++] = num and 4 > 0
            matrix[i][j++] = num and 2 > 0
            matrix[i][j++] = num and 1 > 0
        }
    }

    return matrix
}
