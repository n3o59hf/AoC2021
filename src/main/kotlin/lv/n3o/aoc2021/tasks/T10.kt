package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T10(input: Input) : Task(input) {
    private var scoring = mapOf(
        '(' to 1L, '[' to 2L, '{' to 3L, '<' to 4L, ')' to 3L, ']' to 57L, '}' to 1197L, '>' to 25137L
    )
    private val matching = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    private val openings = matching.keys
    private val closings = matching.values.toSet()

    private val syntaxErrorScores: List<Long>
    private val incompleteScores: List<Long>

    init {
        val syntaxErrors = mutableListOf<Long>()
        val incompletes = mutableListOf<Long>()
        input.asLines().forEach { line ->
            val currentOpenings = ArrayDeque<Char>()
            for (c in line) {
                when (c) {
                    in openings -> currentOpenings.add(c)
                    in closings -> if (c != matching[currentOpenings.removeLast()]) {
                        syntaxErrors.add(scoring[c] ?: error("Wrong character"))
                        return@forEach
                    }
                }
            }
            incompletes.add(generateSequence { currentOpenings.removeLastOrNull() }.fold(0L) { score, c ->
                (score * 5L) + (scoring[c] ?: error("Wrong character"))
            })
        }
        syntaxErrorScores = syntaxErrors
        incompleteScores = incompletes
    }

    override fun a() = syntaxErrorScores.sum()

    override fun b() = incompleteScores.sorted().let { it[it.size / 2] }
}