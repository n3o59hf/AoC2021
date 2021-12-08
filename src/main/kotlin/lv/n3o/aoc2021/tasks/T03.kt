package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.transpose


class T03(input: Input) : Task(input) {
    private val data = input.asLines(true)

    override fun a(): Int {
        val bits = data.map { it.toList() }.transpose().map {
                val ones = it.count { c -> c == '1' }
                data.size - ones < ones
            }
        val gamma = bits.joinToString("") { if (it) "0" else "1" }.toInt(2)
        val epsilon = bits.joinToString("") { if (it) "1" else "0" }.toInt(2)

        return gamma * epsilon
    }

    override fun b() = data.filterList(true)[0].toInt(2) * data.filterList(false)[0].toInt(2)

    private tailrec fun List<String>.filterList(oxygen: Boolean, position: Int = 0): List<String> {
        val zeroes = this.count { it[position] == '0' }
        val ones = this.size - zeroes

        val keep = if (zeroes > ones) '0' else '1'

        val result = this.filter { (it[position] == keep) xor !oxygen } // reverse in case of co2
        return if (result.size > 1) result.filterList(oxygen, position + 1) else result
    }
}