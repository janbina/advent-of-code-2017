package day04

import sorted
import uniquePairs
import java.io.File
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = File("data/day04.txt").readLines()
            .map {
                it.split(Regex("\\s+"))
            }

    assertEquals(466, part1(input))
    assertEquals(251, part2(input))
}

fun part1(input: List<List<String>>): Int {
    return input.map {
        it.uniquePairs().none {
            it.first == it.second
        }
    }.count { it }
}

fun part2(input: List<List<String>>): Int {
    return input.map {
        it.uniquePairs().none {
            it.first.sorted() == it.second.sorted()
        }
    }.count { it }
}