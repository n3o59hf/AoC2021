package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T08(input: Input) : Task(input) {
    private val data = input.asLines().map {
            val (patterns, digits) = it.split(" | ")
            DisplayStates(
                patterns.split(" ").map(String::toSet).toSet(), digits.split(" ").map(String::toSet).toList()
            )
        }

    private val simpleDigits = setOf('1', '4', '7', '8')

    override fun a() = data.sumOf { it.decodedNumber.count { c -> c in simpleDigits } }

    override fun b() = data.sumOf { it.decodedInt }

    private data class DisplayStates(val patterns: Set<Set<Char>>, val digits: List<Set<Char>>) {
        private val decoder: Map<Set<Char>, Char>

        init {
            val p1 = patterns.first { it.size == 2 }
            val p7 = patterns.first { it.size == 3 }
            val p4 = patterns.first { it.size == 4 }
            val p8 = patterns.first { it.size == 7 }

            val g5 = patterns.filter { it.size == 5 }
            val g6 = patterns.filter { it.size == 6 }

            val p3 = g5.first { it.containsAll(p1) }
            val p9 = g6.first { it.containsAll(p3) }
            val p0 = g6.first { it != p9 && it.containsAll(p7) }
            val p6 = (g6 - setOf(p0, p9)).first()
            val p2 = (g5.filter { it.containsAll(p8 - p6) }).first { it != p3 }
            val p5 = (g5 - setOf(p2, p3)).first()

            decoder = mapOf(
                p1 to '1',
                p2 to '2',
                p3 to '3',
                p4 to '4',
                p5 to '5',
                p6 to '6',
                p7 to '7',
                p8 to '8',
                p9 to '9',
                p0 to '0',
            )
        }

        val decodedNumber = digits.map { decoder[it] }.joinToString("")
        val decodedInt = decodedNumber.toInt()
    }
}