package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T04(input: Input) : Task(input) {
    private val data = input.asLines(true)
    private val numberSequence = data[0].split(",").map(String::toInt).asSequence()
    private val boardData = data.drop(1).chunked(5)

    private fun createBoards(): List<BingoBoard> {
        return boardData.map(::BingoBoard)
    }

    override fun a(): String {
        val boards = createBoards()

        numberSequence.forEach { n ->
            boards.forEach { it.markNumber(n) }
            val winner = boards.firstOrNull(BingoBoard::winner)
            if (winner != null) {
                return (winner.unmarkedNumbers.sum() * n).toString()
            }
        }

        error("Wrong input")
    }

    override fun b(): String {
        val boards = createBoards().toMutableSet()

        numberSequence.forEach { n ->
            val iterator = boards.iterator()
            while (iterator.hasNext()) {
                val board = iterator.next()
                board.markNumber(n)
                if (board.winner) iterator.remove()
                if (boards.isEmpty()) return (board.unmarkedNumbers.sum() * n).toString()
            }
        }

        error("Wrong input")
    }
}

private class BingoBoard(lines: List<String>) {
    var matches = 0

    val grid = lines.map { it.split(" ").filter(String::isNotEmpty).map(String::toInt) }.flatMapIndexed { row, cells ->
            cells.mapIndexed { column, cell ->
                cell to 1.shl(row * 5 + column)
            }
        }.toMap()
    val key = lines.joinToString()

    val winner: Boolean
        get() {
            var columns = 0b11111
            for (i in 0..4) {
                val row = matches.shr(i * 5) and 0b11111
                if (row == 0b11111) {
                    return true
                }
                columns = columns and row
            }
            return columns > 0
        }

    val unmarkedNumbers get() = grid.entries.filter { (matches and it.value) == 0 }.map { it.key }

    fun markNumber(number: Int) {
        matches = matches or (grid[number] ?: 0)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is BingoBoard) return false
        return key == other.key
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}