import java.util.*
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = getInput(24).readLines().mapIndexed { index, s ->
        s.split("/")
            .map { it.toInt() }
            .let {
                Component(index, it[0], it[1])
            }
    }

    assertEquals(1906 to 1824, part1and2(input))
}

data class Component(
    val id: Int,
    val a: Int,
    val b: Int
)

class State(
    val strength: Int,
    val length: Int,
    val end: Int,
    val visited: Set<Component>
)

fun part1and2(components: List<Component>): Pair<Int, Int> {
    val map = mutableMapOf<Int, MutableList<Component>>()

    components.forEach {
        map.getOrPut(it.a) { mutableListOf() }.add(it)
        map.getOrPut(it.b) { mutableListOf() }.add(it)
    }

    var maxStrength = 0
    var maxLength = 0
    var maxLenMaxStrength = 0

    val stack: Deque<State> = LinkedList()

    stack.add(State(0, 0, 0, hashSetOf()))

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()

        if (current.strength > maxStrength) {
            maxStrength = current.strength
        }
        if (current.length > maxLength || (current.length == maxLength && current.strength > maxLenMaxStrength)) {
            maxLength = current.length
            maxLenMaxStrength = current.strength
        }

        map[current.end]
            ?.filter { !current.visited.contains(it) }
            ?.forEach {
                stack.add(
                    State(
                        current.strength + it.a + it.b,
                        current.length + 1,
                        if (current.end == it.a) it.b else it.a,
                        current.visited + it
                    )
                )
            }
    }

    return maxStrength to maxLenMaxStrength
}