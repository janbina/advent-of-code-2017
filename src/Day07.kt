package day07

import getInput
import kotlin.math.abs
import kotlin.test.assertEquals

data class Program(
        val name: String,
        val weight: Int,
        val supports: MutableList<Program> = mutableListOf(),
        var supportedBy: Program? = null
) {
    private val totalWeight: Int by lazy {
        weight + supports.sumBy { it.totalWeight }
    }

    private val childrenAreBalanced: Boolean by lazy {
        supports.distinctBy { it.totalWeight }.size == 1
    }

    fun findImbalance(imbalance: Int? = null): Int {
        if (imbalance != null && childrenAreBalanced) {
            return weight - imbalance
        } else {
            val grouped = supports.groupBy { it.totalWeight }
            val badOne = grouped.minBy { it.value.size }!!.value.first()
            val imb = grouped.keys.toList().let { abs(it[0] - it[1]) }

            return badOne.findImbalance(imb)
        }
    }
}

fun parseInput(lines: List<String>): Program {
    val programs = mutableMapOf<String, Program>()
    val parentToChild = mutableSetOf<Pair<Program, String>>()

    lines.forEach {
        val parsed = Regex("\\w+").findAll(it).map { it.value }.toList()
        val program = Program(parsed[0], parsed[1].toInt())

        programs.put(program.name, program)
        parsed.drop(2).forEach {
            parentToChild.add(program to it)
        }
    }

    parentToChild.forEach { (parent, child) ->
        programs[child]?.let {
            parent.supports.add(it)
            it.supportedBy = parent
        }
    }

    return programs.values.first { it.supportedBy == null }
}

fun main(args: Array<String>) {
    val input = parseInput(getInput(7).readLines())

    assertEquals("eugwuhl", input.name)
    assertEquals(420, input.findImbalance())
}