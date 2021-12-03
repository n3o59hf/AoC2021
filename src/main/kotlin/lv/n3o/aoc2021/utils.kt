package lv.n3o.aoc2021

import lv.n3o.aoc2021.coords.C2
import kotlin.math.abs
import kotlin.math.roundToInt

private val startTime = System.nanoTime()

val timeFromApplicationStart get() = System.nanoTime() - startTime

fun Long.formatTime(): String = (this / 1000000.0).roundToInt().toString()
    .padStart(6, ' ')
    .chunked(3)
    .joinToString(" ") + "ms"

val String.cleanLines get() = lines().map { it.trim() }.filter { it.isNotBlank() }

fun <T> List<T>.infinite() = sequence {
    while (true) {
        yieldAll(this@infinite)
    }
}

fun <T> List<T>.permute(): List<List<T>> {
    if (size == 1) return listOf(this)

    val permutations = mutableListOf<List<T>>()
    val movableElement = first()
    for (p in drop(1).permute())
        for (i in 0..p.size) {
            val mutation = p.toMutableList()
            mutation.add(i, movableElement)
            permutations.add(mutation)
        }
    return permutations
}

fun <T> List<T>.combinations(): Sequence<Set<T>> = sequence {
    var indexes = intArrayOf()
    while (indexes.size < this@combinations.size) {
        indexes = IntArray(indexes.size + 1) { it }
        while (indexes[0] <= this@combinations.size - indexes.size) {
            yield(indexes.map { this@combinations[it] }.toSet())
            var incrementIndex = indexes.size - 1
            while (incrementIndex >= 0) {
                indexes[incrementIndex] += 1
                if (indexes[incrementIndex] > this@combinations.size - (indexes.size - incrementIndex)) {
                    incrementIndex--
                } else {
                    break
                }
            }
            incrementIndex++
            while (incrementIndex < indexes.size) {
                if (incrementIndex != 0)
                    indexes[incrementIndex] = indexes[incrementIndex - 1] + 1
                incrementIndex++
            }
        }
    }
}

fun gcd(a: Int, b: Int): Int {
    var gcd = a.coerceAtMost(b)
    while (gcd > 0) {
        if (a % gcd == 0 && b % gcd == 0)
            return gcd
        gcd--
    }
    return -1
}

fun gcd(a: Long, b: Long): Long {
    var gcd = a.coerceAtMost(b)
    while (gcd > 0L) {
        if (a % gcd == 0L && b % gcd == 0L)
            return gcd
        gcd--
    }
    return -1
}

fun lcm(a: Long, b: Long): Long = abs(a * b) / gcd(a, b)

fun Long.divRoundedUp(divider: Long) = this.toNearestMultipleUp(divider) / divider

fun Long.toNearestMultipleUp(factor: Long): Long {
    val reminder = if (this % factor > 0) 1 else 0
    return ((this / factor) + reminder) * factor
}

fun <T> Map<C2, T>.debugDraw(cellWidth: Int = 1, conversion: (T?) -> Any = { it.toString() }) {
    val allKeys = keys

    val maxX = allKeys.map(C2::x).maxOrNull() ?: 1
    val maxY = allKeys.map(C2::y).maxOrNull() ?: 1
    val minX = allKeys.map(C2::x).minOrNull() ?: 1
    val minY = allKeys.map(C2::y).minOrNull() ?: 1


    val cellBorder = (0 until cellWidth).joinToString("") { "-" }
    val verticalSeperator = "\n" + (minX..maxX).joinToString("+", "+", "+") { cellBorder } + "\n"

    val output = "\n$verticalSeperator" + (minY..maxY).map { y ->
        (minX..maxX).map { x ->
            var cell = conversion(this[C2(x, y)]).toString()
            cell = cell.substring(0, cell.length.coerceAtMost(cellWidth))
            if (cell.length < cellWidth)
                cell = cell.padEnd(cell.length + ((cellWidth - cell.length) / 2))
            if (cell.length < cellWidth)
                cell = cell.padStart(cellWidth)
            cell
        }.joinToString("|", "|", "|")

    }.joinToString(verticalSeperator) + verticalSeperator
    println("\n$output\n")
}

class MapWithLazy<K, V>(val backingMap: MutableMap<K, V>, val lazy: (K) -> V) : MutableMap<K, V> by backingMap {
    override operator fun get(key: K): V {
        val value = backingMap[key]
        return if (value == null) {
            val lazyValue = lazy(key)
            backingMap[key] = lazyValue
            lazyValue
        } else {
            value
        }
    }
}

fun <K, V> MutableMap<K, V>.withLazy(lazy: (K) -> V) = MapWithLazy(this, lazy)

fun <V> Map<C2, V>.infinite(horizontal: Boolean = false, vertical: Boolean = false) =
    InfiniteMap(this, horizontal, vertical)

class InfiniteMap<V>(val original: Map<C2, V>, val horizontal: Boolean, val vertical: Boolean) {
    val realMinX = original.keys.map { it.x }.minOrNull() ?: error("No data")
    val realMinY = original.keys.map { it.y }.minOrNull() ?: error("No data")
    val realMaxX = original.keys.map { it.x }.maxOrNull() ?: error("No data")
    val realMaxY = original.keys.map { it.y }.maxOrNull() ?: error("No data")
    val xRange = realMinX..realMaxX
    val yRange = realMinY..realMaxY

    operator fun get(c: C2): V {
        val x = when {
            c.x in xRange -> c.x
            c.x < realMinX && horizontal -> {
                var x = c.x
                val diff = xRange.count()
                while (x !in xRange) x += diff
                x
            }
            c.x > realMaxX && horizontal -> {
                var x = c.x
                val diff = xRange.count()
                while (x !in xRange) x -= diff
                x
            }
            else -> error("X out of range")
        }
        val y = when {
            c.y in yRange -> c.y
            c.y < realMinY && horizontal -> {
                var y = c.y
                val diff = yRange.count()
                while (y !in yRange) y += diff
                y
            }
            c.y > realMaxY && horizontal -> {
                var y = c.y
                val diff = yRange.count()
                while (y !in yRange) y -= diff
                y
            }
            else -> error("Y out of range")
        }

        return original[C2(x, y)] ?: error("Non-square coordinate grid")
    }

    fun contains(coord: C2) =
        (horizontal || coord.x in xRange) && (vertical || coord.y in yRange)
}

fun <E> List<List<E>>.transpose() = List(this[0].size) { i -> this.map { it[i] } }
