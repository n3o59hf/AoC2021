package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T06(input: Input) : Task(input) {
    private val data = input.raw().split(",").map(String::toInt).let { fishStates ->
        LongArray(9) { 0 }.also { array ->
            fishStates.forEach { i -> array[i]++ }
        }
    }

    override fun a() = calculateFishes(80)

    override fun b() = calculateFishes(256)

    private fun calculateFishes(days: Int): Long {
        val fishCounts = LongArray(9) { data[it] }

        repeat(days) {
            val newFishes = fishCounts[0]
            System.arraycopy(fishCounts, 1, fishCounts, 0, 8)
            fishCounts[8] = newFishes
            fishCounts[6] += newFishes
        }

        return fishCounts.sum()
    }
}