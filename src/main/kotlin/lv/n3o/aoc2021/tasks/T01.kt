package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task


class T01(input: Input) : Task(input) {
    private val data = input.asListOfInts()

    override fun a() = data.countIncreases()

    override fun b()= data.windowed(3).map { it.sum() }.countIncreases()

    private fun List<Int>.countIncreases() = this.zipWithNext().map { it.second - it.first }.count { it > 0 }
}