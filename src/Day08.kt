package day08

import getInput
import kotlin.test.assertEquals

data class Instruction(
        val name: String,
        val operation: (Int) -> Int,
        val condName: String,
        val condition: (Int) -> Boolean
) {
    companion object {
        fun fromString(input: String): Instruction {
            val parts = input.split(Regex("\\s+"))
            val condition: (Int) -> Boolean = when (parts[5]) {
                "<" -> { a -> a < parts[6].toInt() }
                ">" -> { a -> a > parts[6].toInt() }
                "<=" -> { a -> a <= parts[6].toInt() }
                ">=" -> { a -> a >= parts[6].toInt() }
                "==" -> { a -> a == parts[6].toInt() }
                "!=" -> { a -> a != parts[6].toInt() }
                else -> throw IllegalArgumentException("Invalid condition operator: ${parts[5]}")
            }
            val operation: (Int) -> Int = when (parts[1]) {
                "inc" -> { a -> a + parts[2].toInt() }
                "dec" -> { a -> a - parts[2].toInt() }
                else -> throw IllegalArgumentException("Invalid operator: ${parts[1]}")
            }
            return Instruction(parts[0], operation, parts[4], condition)
        }
    }
}

fun main(args: Array<String>) {
    val input = getInput(8).readLines().map { Instruction.fromString(it) }

    val result = execute(input)

    assertEquals(5215, result.first)
    assertEquals(6419, result.second)
}

fun execute(input: List<Instruction>): Pair<Int, Int> {
    val registers = mutableMapOf<Int, Int>()
    var maxValue = 0

    for (inst in input) {
        val condValue = registers.getOrDefault(inst.condName.hashCode(), 0)
        val targetValue = registers.getOrDefault(inst.name.hashCode(), 0)

        if (inst.condition(condValue)) {
            val newValue = inst.operation(targetValue)

            registers.put(inst.name.hashCode(), newValue)
            if (newValue > maxValue) {
                maxValue = newValue
            }
        }
    }

    val endMax = registers.values.max() ?: 0
    return endMax to maxValue
}