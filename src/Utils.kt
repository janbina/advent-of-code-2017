import java.io.File

fun getInput(dayNum: Int): File {
    val paddedDayNum = dayNum.toString().padStart(2, '0')
    return File("data/day$paddedDayNum.txt")
}

fun <T> List<T>.uniquePairs(): List<Pair<T, T>> {
    return this.asSequence().uniquePairs().toList()
}

fun <T> Sequence<T>.uniquePairs(): Sequence<Pair<T, T>> {
    return this.mapIndexedNotNull { i, item1 ->
        this.mapIndexedNotNull { j, item2 ->
            if (i < j) {
                item1 to item2
            } else {
                null
            }
        }
    }.flatten()
}

fun String.sorted(): String {
    return this.toList().sorted().joinToString(separator = "")
}