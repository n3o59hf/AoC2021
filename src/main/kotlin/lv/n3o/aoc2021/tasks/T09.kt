package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C2
import lv.n3o.aoc2021.setDefault

class T09(input: Input) : Task(input) {
    private val map = input.asCoordGrid().mapValues { it.value.digitToInt() }.setDefault(9)
    private val lowPoints = map.filter { current -> current.value < current.key.neighbors4().minOf { map[it] } }.keys

    override fun a() = lowPoints.sumOf { map[it] +1 }

    override fun b(): Int {
        val basin = mutableMapOf<C2, Int>().setDefault(0)
        for (height in 8 downTo 0) {
            val heightPoints = map.filter { it.value == height }.keys

            for (point in heightPoints) {
                val direction = point.neighbors4().minByOrNull { map[it] } ?: error("Wrong coordinate")
                if (map[direction] < map[point]) {
                    basin[direction] += basin[point] + 1
                } else {
                    basin[point]++
                }
            }
        }

        return lowPoints.map { basin[it] }.sorted().takeLast(3).fold(1) { a, b -> a * b }
    }
}