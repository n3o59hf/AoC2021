package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C3
import lv.n3o.aoc2021.signLong
import kotlin.math.max
import kotlin.math.min

class T22(input: Input) : Task(input) {
    private val centerFrom = C3(-50, -50, -50)
    private val centerTo = C3(51, 51, 51)
    private val instructions = input.asLines().map { l ->
        val (power, coords) = l.split(" ")
        val (x, y, z) = coords.split(",").map { it.substring(2).split("..").map(String::toInt) }
        Instruction(power == "on", C3(x[0], y[0], z[0]), C3(x[1], y[1], z[1]) + C3(1, 1, 1))
    }.fold(listOf<Instruction>()) { list, i ->
        list + list.mapNotNull { j -> j.invert(i) } + if (i.power) listOf(i) else listOf()
    }

    inner class Instruction(val power: Boolean, val from: C3, val to: C3) {
        val impact get() = (to - from).vectorVolumeLong() * power.signLong
        val inCenter = from.inBoxInclusive(centerFrom, centerTo) && to.inBoxInclusive(centerFrom, centerTo)
        fun invert(other: Instruction): Instruction? {
            val from = C3(max(from.x, other.from.x), max(from.y, other.from.y), max(from.z, other.from.z))
            val to = C3(min(to.x, other.to.x), min(to.y, other.to.y), min(to.z, other.to.z))
            return if (from.x <= to.x && from.y <= to.y && from.z <= to.z) Instruction(!power, from, to) else null
        }
    }

    override fun a() = instructions.filter(Instruction::inCenter).sumOf(Instruction::impact)

    override fun b() = instructions.sumOf(Instruction::impact)
}