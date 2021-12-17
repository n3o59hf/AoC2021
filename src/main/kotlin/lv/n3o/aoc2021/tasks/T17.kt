package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C2
import kotlin.math.absoluteValue
import kotlin.math.sign

class T17(input: Input) : Task(input) {
    private val data =
        input.raw().replace("target area: x=", "").replace("y=", "").split(", ").flatMap { it.split("..") }
            .map { it.toInt() }
    private val min = C2(data[0], data[2])
    private val max = C2(data[1], data[3])

    override fun a() = ((min.y + 1)..0).sum().absoluteValue

    override fun b() = (min.y..min.y.absoluteValue).flatMap { dY -> (0..max.x).map { dX -> C2(dX, dY) } }.count { dV ->
        var c = C2(0, 0)
        var v = dV
        while (c.x <= max.x && c.y >= min.y && (c.x >= min.x || v.x != 0)) {
            c += v
            v = C2(v.x - v.x.sign, v.y - 1)
            if (c.inSquareInclusive(min, max)) return@count true
        }
        return@count false
    }
}
