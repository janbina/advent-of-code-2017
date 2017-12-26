package Day25

import java.util.*
import kotlin.test.assertEquals

fun main(args: Array<String>) {

    //INPUT:
    val initialState = 1
    val steps = 12_172_063
    val transitions = listOf(
            TuringMachine.Transition(1, false, 2, true, TuringMachine.Transition.Move.RIGHT),
            TuringMachine.Transition(1, true, 3, false, TuringMachine.Transition.Move.LEFT),
            TuringMachine.Transition(2, false, 1, true, TuringMachine.Transition.Move.LEFT),
            TuringMachine.Transition(2, true, 4, true, TuringMachine.Transition.Move.LEFT),
            TuringMachine.Transition(3, false, 4, true, TuringMachine.Transition.Move.RIGHT),
            TuringMachine.Transition(3, true, 3, false, TuringMachine.Transition.Move.RIGHT),
            TuringMachine.Transition(4, false, 2, false, TuringMachine.Transition.Move.LEFT),
            TuringMachine.Transition(4, true, 5, false, TuringMachine.Transition.Move.RIGHT),
            TuringMachine.Transition(5, false, 3, true, TuringMachine.Transition.Move.RIGHT),
            TuringMachine.Transition(5, true, 6, true, TuringMachine.Transition.Move.LEFT),
            TuringMachine.Transition(6, false, 5, true, TuringMachine.Transition.Move.LEFT),
            TuringMachine.Transition(6, true, 1, true, TuringMachine.Transition.Move.RIGHT)
    )

    val machine = TuringMachine(initialState, transitions)
    machine.run(steps)

    assertEquals(2474, machine.cardinality)
}

class TuringMachine(initialState: Int, transitions: List<Transition>) {

    private val tape = BitSet()
    private val transitions = transitions.groupBy { it.fromState }
    private val tapePosition get() =  if (position <= 0) -position * 2 else position * 2 - 1
    private val transition get() = transitions[state]!!.first { it.fromValue == tape[tapePosition] }

    private var position = 0
    private var state = initialState

    val cardinality get() = tape.cardinality()

    fun run(steps: Int) {
        repeat(steps) {
            transition.let {
                state = it.toState
                tape[tapePosition] = it.toValue
                position += it.move.move
            }
        }
    }

    data class Transition(
            val fromState: Int,
            val fromValue: Boolean,
            val toState: Int,
            val toValue: Boolean,
            val move: Move
    ) {
        enum class Move(val move: Int) {
            LEFT(-1), RIGHT(1)
        }
    }
}