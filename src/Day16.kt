package day16

import getInput
import java.util.*
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(16).readLines().first().split(',').map { Instruction.fromString(it) }
    val programs = CharArray(16) { (it + 'a'.toInt()).toChar() }

    assertEquals("ociedpjbmfnkhlga", part1(programs, input))
    assertEquals("gnflbkojhicpmead", part2(programs, input))
}

sealed class Instruction {

    companion object {
        fun fromString(input: String): Instruction {
            val first = input[0]
            val rest = input.substring(1).split('/')
            return when (first) {
                's' -> Spin(rest[0].toInt())
                'x' -> Exchange(rest[0].toInt(), rest[1].toInt())
                'p' -> Partner(rest[0][0], rest[1][0])
                else -> throw IllegalArgumentException(input)
            }
        }
    }

    abstract fun apply(input: CharArray): CharArray

    data class Spin(private val x: Int) : Instruction() {
        override fun apply(input: CharArray): CharArray {
            if (input.size < x) throw IllegalArgumentException()
            val p1 = input.copyOfRange(input.size - x, input.size)
            val p2 = input.copyOfRange(0, input.size - x)

            return p1 + p2
        }
    }

    data class Exchange(private val x: Int, private val y: Int) : Instruction() {
        override fun apply(input: CharArray): CharArray {
            if (x !in input.indices || y !in input.indices) throw IllegalArgumentException()
            val copy = input.copyOf()
            copy[x].let {
                copy[x] = copy[y]
                copy[y] = it
            }

            return copy
        }
    }

    data class Partner(private val x: Char, private val y: Char) : Instruction() {
        override fun apply(input: CharArray):CharArray {
            val i = input.indexOf(x)
            val j = input.indexOf(y)
            if (i !in input.indices || j !in input.indices) throw IllegalArgumentException()
            val copy = input.copyOf()
            copy[i].let {
                copy[i] = copy[j]
                copy[j] = it
            }

            return copy
        }
    }
}

fun part1(programs: CharArray, instructions: List<Instruction>): String {
    val transformed = instructions.fold(programs, {acc, inst -> inst.apply(acc)})
    return transformed.joinToString(separator = "")
}

fun part2(programs: CharArray, instructions: List<Instruction>): String {
    var transformed = programs.copyOf()
    val transformations = mutableListOf<String>()

    (1..Int.MAX_VALUE).forEach {
        transformed = instructions.fold(transformed, {acc, inst -> inst.apply(acc)})
        if (Arrays.equals(transformed, programs)) {
            return transformations[1_000_000_000 % it - 1]
        }
        transformations.add(transformed.joinToString(separator = ""))
    }

    return ""
}
