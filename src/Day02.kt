package day02

import java.io.File

fun main(args: Array<String>) {
    val input = File("data/day02.txt").readLines()
            .map {
                it.split(Regex("\\s+")).map { it.toInt() }
            }

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<List<Int>>): Int {
    return input.sumBy { it.max()!! - it.min()!! }
}

fun part2(input: List<List<Int>>): Int {
    return input.map { it.sortedDescending() }
            .sumBy { line ->
                line.indices.sumBy { i ->
                    (i+1 until line.size)
                            .filter { j -> line[i] % line[j] == 0 }
                            .sumBy { j -> line[i] / line[j] }
                }
            }
}