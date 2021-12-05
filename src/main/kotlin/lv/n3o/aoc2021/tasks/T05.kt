package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C2
import kotlin.math.abs

class T05(input: Input) : Task(input) {
    private val data = input
        .asLines(true)
        .map {
            val (start, end) = it.split(" -> ").map { c ->
                val (x, y) = c.split(",").map(String::toInt)
                C2(x, y)
            }
            start to end
        }

    private val cardinalLines: List<C2>
    private val diagonalLines: List<C2>

    init {
        val (c, d) = data.partition { (a, b) -> a.x == b.x || a.y == b.y }
        cardinalLines = c.flatMap { (a, b) -> makeLine(a, b) }
        diagonalLines = d.flatMap { (a, b) -> makeLine(a, b) }
    }


    override fun a(): String {
        return countRepeated(cardinalLines).toString()
    }

    override fun b(): String {
        return countRepeated(cardinalLines + diagonalLines).toString()
    }

    private fun countRepeated(list: List<C2>): Int {
        var repeatedCount = 0
        var current: C2? = null
        var hasRepeated = false

        for (c in list.sorted()) {
            if (current != c) {
                current = c
                hasRepeated = false
            } else if (!hasRepeated) {
                hasRepeated = true
                repeatedCount++
            }
        }

        return repeatedCount
    }

    private fun makeLine(from: C2, to: C2): List<C2> {
        val direction = (to - from).unit()
        val pointCount = abs(from.x - to.x).coerceAtLeast(abs(from.y - to.y)) + 1
        return List(pointCount) { i -> from + direction.times(i) }

    }
}