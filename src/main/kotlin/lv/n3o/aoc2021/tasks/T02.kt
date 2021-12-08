package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import java.lang.Exception

class T02(input: Input) : Task(input) {
    private val data = input.asLines(true).map {
            it.trim().split(" ").let { (direction, x) -> direction to x.toInt() }
        }

    override fun a(): Int {
        var depth = 0
        var horizontal = 0

        for ((direction, x) in data) {
            when (direction) {
                "down" -> depth += x
                "up" -> depth -= x
                "forward" -> horizontal += x
            }
        }

        return depth * horizontal
    }

    override fun b(): Int {
        var depth = 0
        var horizontal = 0
        var aim = 0

        for ((direction, x) in data) {
            when (direction) {
                "down" -> aim += x
                "up" -> aim -= x
                "forward" -> {
                    horizontal += x
                    depth += aim * x
                }
            }
        }

        return depth * horizontal
    }
}