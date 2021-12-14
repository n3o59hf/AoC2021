package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.divRoundedUp

class T14(input: Input) : Task(input) {
    private val data = input.asLinesPerBlock()
    private val polymer = data[0][0].windowed(2, 1).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    private val insertions = data.last().associate { l -> l.split(" -> ").let { (a, b) -> a to b } }

    private val polymerGrowth = generateSequence(polymer) {
        it.flatMap { (pair, count) ->
            val insert = insertions[pair] ?: error("Pair not defined $pair")
            listOf("${pair[0]}$insert" to count, "$insert${pair[1]}" to count)
        }.groupingBy { (k, _) -> k }.fold(0L) { a, (_, c) -> a + c }
    }

    private fun Map<String, Long>.findDifference() =
        flatMap { (pair, c) -> listOf(pair[0] to c, pair[1] to c) }.groupBy({ it.first }, { it.second })
            .map { it.value.sum().divRoundedUp(2) }.let { (it.maxOrNull() ?: 0) - (it.minOrNull() ?: 0) }

    override fun a() = polymerGrowth.elementAt(10).findDifference()

    override fun b() = polymerGrowth.elementAt(40).findDifference()
}