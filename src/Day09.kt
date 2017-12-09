package day09

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(9).readText()

    val result = analyze(input)

    assertEquals(16021, result.first)
    assertEquals(7685, result.second)
}

fun analyze(input: String): Pair<Int, Int> {
    var totalScore = 0
    var currentScore = 0
    var totalGarbage = 0
    var inGarbage = false
    var ignoreNext = false

    for (c in input) {
        if (ignoreNext) {
            ignoreNext = false
            continue
        }

        if (inGarbage && c == '!') {
            ignoreNext = true
            continue
        }

        if (inGarbage && c == '>') {
            inGarbage = false
            continue
        }

        if (!inGarbage && c == '<') {
            inGarbage = true
            continue
        }

        if (inGarbage) {
            totalGarbage++
            continue
        }

        if (c == '{') {
            currentScore++
        }

        if (c == '}') {
            totalScore += currentScore
            currentScore--
        }
    }

    return totalScore to totalGarbage
}
