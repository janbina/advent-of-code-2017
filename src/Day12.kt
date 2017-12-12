package day12

import getInput
import java.util.*
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(12)
            .readLines()
            .map { Regex("\\w+").findAll(it).map { it.value.toInt() }.toList() }

    val nodes = parseInput(input)

    assertEquals(175, part1(nodes))
    assertEquals(213, part2(nodes))
}

fun parseInput(input: List<List<Int>>): Map<Int, Set<Int>> {
    val map = mutableMapOf<Int, MutableSet<Int>>()

    input.forEach { line ->
        val node = map.computeIfAbsent(line[0]) { mutableSetOf() }
        line.drop(1).forEach { node.add(it)}
    }

    return map
}

fun getGroup(nodes: Map<Int, Set<Int>>, start: Int): Set<Int> {
    val group = mutableSetOf<Int>()
    val queue = LinkedList<Int>()

    queue.add(start)

    while (queue.isNotEmpty()) {
        queue.pop().let {
            if (group.add(it)) {
                nodes[it]!!.forEach { queue.add(it) }
            }
        }
    }

    return group
}

fun part1(nodes: Map<Int, Set<Int>>): Int {
    return getGroup(nodes, 0).size
}

fun part2(nodes: Map<Int, Set<Int>>): Int {

    val inGroup = mutableSetOf<Int>()
    var groups = 0

    while (inGroup.size != nodes.size) {
        nodes.keys.first { !inGroup.contains(it) }.let {
            inGroup.addAll(getGroup(nodes, it))
            groups++
        }
    }

    return groups
}
