package Day20

import getInput
import kotlin.math.abs
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(20).readLines()
            .map { Regex("-?\\d+")
                    .findAll(it)
                    .map { it.value.toLong() }
                    .toList()
            }
            .mapIndexed { index, it -> Point(index, Triple(it[0], it[1], it[2]), Triple(it[3], it[4], it[5]), Triple(it[6], it[7], it[8])) }

    assertEquals(125, part1(input))
    assertEquals(461, part2(input))
}

data class Point(
        val id: Int,
        var position: Triple<Long, Long, Long>,
        var velocity: Triple<Long, Long, Long>,
        val acceleration: Triple<Long, Long, Long>
) {
    fun tick() {
        velocity += acceleration
        position += velocity
    }
}

fun part1(input: List<Point>): Int {

    return input.sortedWith(compareBy({it.acceleration.absSum()}, {it.velocity.absSum()}, {it.position.absSum()}))
            .first().id
}

fun part2(input: List<Point>): Int {
    var particles = input
    var lastValue = -1
    var sameCount = 0
    val sameThreshold = 30

    while (true) {

        particles = particles.groupBy { it.position }
                .filter { it.value.size == 1 }
                .values
                .map { it.first() }

        if (particles.size == lastValue) {
            sameCount++
            if (sameCount > sameThreshold) {
                return particles.size
            }
        } else {
            sameCount = 0
            lastValue = particles.size
        }

        input.forEach { it.tick() }
    }
}

operator fun Triple<Long, Long, Long>.plus(other: Triple<Long, Long, Long>): Triple<Long, Long, Long> {
    return Triple(this.first + other.first, this.second + other.second, this.third + other.third)
}

fun Triple<Long, Long, Long>.absSum(): Long {
    return abs(this.first) + abs(this.second) + abs(this.third)
}