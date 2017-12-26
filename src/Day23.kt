package Day23

import getInput
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(23).readLines().map { Instruction.fromString(it) }

    assertEquals(6241, part1(input))
}

sealed class Instruction {

    companion object {
        fun fromString(input: String): Instruction {
            val instr = input.split(Regex("\\s+"))
            return when (instr[0]) {
                "set" -> Set(instr[1], instr[2])
                "sub" -> BinaryOperation("sub", instr[1], instr[2], { a, b -> a - b })
                "mul" -> BinaryOperation("mul", instr[1], instr[2], { a, b -> a * b })
                "jnz" -> Jump(instr[1], instr[2], { it != 0L })
                else -> throw IllegalArgumentException("Invalid instruction $input")
            }
        }
    }

    data class Set(val op1: String, val op2: String): Instruction()
    data class BinaryOperation(val name: String, val op1: String, val op2: String, val execute: (Long, Long) -> Long): Instruction()
    data class Jump(val op1: String, val op2: String, val shouldJump: (Long) -> Boolean): Instruction()
}

fun part1(instructions: List<Instruction>): Int {
    return execute(instructions)
}

fun execute(instructions: List<Instruction>, registers: MutableMap<String, Long> = mutableMapOf()): Int {
    var pc = 0
    var mulCounter = 0

    while (pc in instructions.indices) {
        val inst = instructions[pc]
        pc++

        when (inst) {
            is Instruction.Set -> {
                registers.put(inst.op1, registers.getOperandValue(inst.op2))
            }
            is Instruction.BinaryOperation -> {
                registers.put(inst.op1, inst.execute(registers.getOperandValue(inst.op1), registers.getOperandValue(inst.op2)))
                if (inst.name == "mul") mulCounter++
            }
            is Instruction.Jump -> {
                if (inst.shouldJump(registers.getOperandValue(inst.op1))) pc += registers.getOperandValue(inst.op2).toInt() - 1
            }
        }
    }

    return mulCounter
}

fun Map<String, Long>.getOperandValue(operand: String): Long {
    return operand.toLongOrNull() ?: getOrDefault(operand, 0)
}

