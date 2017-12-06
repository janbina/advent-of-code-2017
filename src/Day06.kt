package day06

import getInput
import kotlin.math.max
import kotlin.math.min
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(6).readLines().first()
            .split(Regex("\\s+"))
            .map { it.toInt() }

    val solution = realloc(input)

    assertEquals(6681, solution.first)
    assertEquals(2392, solution.second)
}

fun realloc(input: List<Int>): Pair<Int, Int> {
    val memory = input.toMutableList()
    val map = mutableMapOf<Int, Int>()
    var steps = 0

    while (true) {
        map.put(memory.hashCode(), steps)?.let {
            return steps to (steps - it)
        }

        var (maxIndex, maxValue) = memory.withIndex().maxBy { it.value }!!
        val piece = max(maxValue / memory.size, 1)

        memory[maxIndex] = 0
        var index = maxIndex + 1

        while (maxValue > 0) {
            min(maxValue, piece).let {
                maxValue -= it
                memory[index % memory.size] += it
            }
            index++
        }

        steps++
    }
}
