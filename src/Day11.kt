package day11

import getInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(11).readLines().first().split(",")

    val result = countDistance(input)

    assertEquals(818, result.first)
    assertEquals(1596, result.second)
}


fun countDistance(input: List<String>): Pair<Int, Int> {
    var x = 0
    var y = 0
    var z = 0
    var currentDistance = 0
    var maxDistance = 0

    input.forEach {
        when (it) {
            "n" ->  {x++; y--}
            "ne" -> {y--; z++}
            "se" -> {x--; z++}
            "s" ->  {x--; y++}
            "sw" -> {y++; z--}
            "nw" -> {x++; z--}
        }

        currentDistance = (abs(x) + abs(y) + abs(z)) / 2
        maxDistance = max(currentDistance, maxDistance)
    }

    return currentDistance to maxDistance
}