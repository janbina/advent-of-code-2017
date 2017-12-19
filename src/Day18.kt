package day18

import getInput
import java.util.*
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(18).readLines().map { Instruction.fromString(it) }

    assertEquals(3188, part1(input))
    assertEquals(7112, part2(input))
}

sealed class Instruction {

    companion object {
        fun fromString(input: String): Instruction {
            val instr = input.split(Regex("\\s+"))
            return when (instr[0]) {
                "set" -> Set(instr[1], instr[2])
                "add" -> BinaryOperation(instr[1], instr[2], { a, b -> a + b })
                "mul" -> BinaryOperation(instr[1], instr[2], { a, b -> a * b })
                "mod" -> BinaryOperation(instr[1], instr[2], { a, b -> a % b })
                "snd" -> Send(instr[1])
                "rcv" -> Receive(instr[1])
                "jgz" -> Jump(instr[1], instr[2], { it > 0 })
                else -> throw IllegalArgumentException("Invalid instruction $input")
            }
        }
    }

    data class Set(val op1: String, val op2: String): Instruction()
    data class BinaryOperation(val op1: String, val op2: String, val execute: (Long, Long) -> Long): Instruction()
    data class Jump(val op1: String, val op2: String, val shouldJump: (Long) -> Boolean): Instruction()
    data class Send(val op1: String): Instruction()
    data class Receive(val op1: String): Instruction()
}

class Program(
        id: Int,
        private val instructions: List<Instruction>,
        var sendFunc: (Long) -> Unit = {},
        var receiveFunc: (Long) -> Boolean = { false }
) {
    private val registers = mutableMapOf("p" to id.toLong())
    private val receiveQueue = LinkedList<Long>()
    private var pc = 0

    fun receive(value: Long) {
        receiveQueue.add(value)
    }

    fun step(): Boolean {
        if (pc !in instructions.indices) return false

        val inst = instructions[pc]
        pc++

        when (inst) {
            is Instruction.Set -> {
                registers.put(inst.op1, getValue(inst.op2))
            }
            is Instruction.BinaryOperation -> {
                registers.put(inst.op1, inst.execute(getValue(inst.op1), getValue(inst.op2)))
            }
            is Instruction.Jump -> {
                if (inst.shouldJump(getValue(inst.op1))) pc += getValue(inst.op2).toInt() - 1
            }
            is Instruction.Send -> {
                send(getValue(inst.op1))
            }
            is Instruction.Receive -> {
                val processed = receiveFunc(getValue(inst.op1))

                if (!processed) {
                    if (receiveQueue.isNotEmpty()) {
                        registers.put(inst.op1, receiveQueue.pop())
                    } else {
                        pc--
                        return false
                    }
                }
            }
        }

        return true
    }

    private fun send(value: Long) {
        sendFunc(value)
    }

    private fun getValue(operand: String): Long {
        return operand.toLongOrNull() ?: registers.getOrDefault(operand, 0)
    }
}

fun part1(instructions: List<Instruction>): Long {
    var sound = -1L
    var run = true

    val program = Program(0, instructions, { sound = it }) {
        if (it != 0L) run = false
        true
    }

    while (run) {
        program.step()
    }

    return sound
}

fun part2(instructions: List<Instruction>): Int {
    val program0 = Program(0, instructions)
    val program1 = Program(1, instructions)
    var sendCounter = 0

    program0.sendFunc = { program1.receive(it) }
    program1.sendFunc = {
        sendCounter++
        program0.receive(it)
    }

    while (program0.step() || program1.step()) {}

    return sendCounter
}



