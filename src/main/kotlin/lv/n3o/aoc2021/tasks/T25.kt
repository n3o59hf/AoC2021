package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T25(input: Input) : Task(input) {
    private val data = input.asLines().map { it.toList() }
    private val width = data[0].size
    private val height = data.size
    private val size = width * height
    private val Int.east get() = (this % width).let { x -> this - x + (x + 1) % width }
    private val Int.north get() = (this + width) % size

    override fun a(): Int {
        val field = CharArray(size) { i -> data[i / width][i % width] }
        var moves = 0

        do {
            val east = field.withIndex().filter { (i, c) -> c == '>' && field[i.east] == '.' }
            east.forEach { (i, c) ->
                field[i] = '.'
                field[i.east] = c
            }

            val north = field.withIndex().filter { (i, c) -> c == 'v' && field[i.north] == '.' }
            north.forEach { (i, c) ->
                field[i] = '.'
                field[i.north] = c
            }

            moves++
        } while (east.isNotEmpty() || north.isNotEmpty())

        return moves
    }
}