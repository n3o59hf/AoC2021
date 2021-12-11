package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T11(input: Input) : Task(input) {
    private val data = input.asCoordGrid().mapValues { it.value.digitToInt() }

    private val energyLevels = generateSequence(data) { input ->
        generateSequence(input.mapValues { (_, v) -> v + 1 }) { octopii ->
            octopii.filter { it.value > 9 }.keys.takeIf { it.isNotEmpty() }?.let { flashesNow ->
                val energized = flashesNow.flatMap { it.neighbors8() }.groupBy { it }

                octopii.mapValues { (k, v) -> if (k in flashesNow) Int.MIN_VALUE else v + (energized[k]?.size ?: 0) }
            }
        }.last().mapValues { (_, v) -> v.coerceAtLeast(0) }
    }

    override fun a() = energyLevels.drop(1).take(100).sumOf { it.count { (_, v) -> v == 0 } }

    override fun b() = energyLevels.withIndex().first { (_, map) -> map.all { (_, v) -> v == 0 } }.index
}