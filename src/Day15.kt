package day15

import getInput
import kotlin.coroutines.experimental.buildSequence
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val (genA, genB) = getInput(15).readLines().map { it.split(" ").last().toLong() }
    val factorA = 16807
    val factorB = 48271
    val module = 2147483647

    assertEquals(577, part1(genA, genB, factorA, factorB, module))
    assertEquals(316, part2(genA, genB, factorA, factorB, module))
}

fun generate(init: Long, factor: Int, module: Int, predicate: (Int) -> Boolean = { true }): Sequence<Int> = buildSequence {
    var value = init

    while (true) {
        value = (value * factor) % module
        value.toInt().let {
            if (predicate(it)) yield(it)
        }
    }
}

fun part1(genAInit: Long, genBInit: Long, factorA: Int, factorB: Int, module: Int): Int {
    val generatorA = generate(genAInit, factorA, module)
            .map { it and 0xFFFF }

    val generatorB = generate(genBInit, factorB, module)
            .map { it and 0xFFFF }

    return generatorA.zip(generatorB).take(40_000_000).count { it.first == it.second }
}

fun part2(genAInit: Long, genBInit: Long, factorA: Int, factorB: Int, module: Int): Int {
    val generatorA = generate(genAInit, factorA, module, { it % 4 == 0 })
            .map { it and 0xFFFF }

    val generatorB = generate(genBInit, factorB, module, { it % 8 == 0 })
            .map { it and 0xFFFF }

    return generatorA.zip(generatorB).take(5_000_000).count { it.first == it.second }
}
