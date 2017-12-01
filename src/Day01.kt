import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val input = Files.readAllLines(Paths.get("data/day01.txt")).first()

    println(part1(input))
    println(part2(input))
}

fun part1(input: String): Int {
    return (input + input.first())
            .zipWithNext()
            .filter { it.first == it.second }
            .sumBy { it.first.toString().toInt() }
}

fun part2(input: String): Int {
    return input
            .zip(input.substring(input.length / 2) + input)
            .filter { it.first == it.second }
            .sumBy { it.first.toString().toInt() }
}