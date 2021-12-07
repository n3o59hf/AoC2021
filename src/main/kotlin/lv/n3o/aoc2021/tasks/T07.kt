package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.nextPowerOf2
import kotlin.math.abs
import kotlin.math.sign

class T07(input: Input) : Task(input) {
    private val data = input.raw().split(",").map(String::toInt).toIntArray().also { it.sort() }
    private val min = data.first()
    private val max = data.last()

    override fun a() = locateMinimum { it }.toString()

    override fun b() = locateMinimum { (it * (it + 1)) / 2 }.toString()

    private inline fun locateMinimum(cost: (Int) -> Int): Int {
        var step = ((max - min) / 2).nextPowerOf2()
        var i = max
        while (step != 0) {
            val current = data.sumOf { j -> cost(abs(j - i)) }
            val a = data.sumOf { j -> cost(abs(j - i - 1)) }
            val b = data.sumOf { j -> cost(abs(j - i + 1)) }
            if (current > a || current > b) {
                i += step * (b - a).sign
            } else {
                break
            }
            step /= 2
        }
        return data.sumOf { j -> cost(abs(j - i)) }
    }
}