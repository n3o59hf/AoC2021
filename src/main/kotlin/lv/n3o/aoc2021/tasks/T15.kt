package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C2
import lv.n3o.aoc2021.coords.C2IntField
import lv.n3o.aoc2021.coords.toIntField
import java.util.*

class T15(input: Input) : Task(input) {
    val data = input.asCoordGrid().mapValues { it.value.digitToInt() }.toIntField(10)
    val start = C2(0, 0)

    fun traverse(risks: C2IntField): Int {
        val to = C2(risks.width - 1, risks.height - 1)
        val field = C2IntField(to, Int.MAX_VALUE / 2)
        field[start] = 0
        val queue = PriorityQueue<C2> { a, b -> field[a].compareTo(field[b]) }
        queue.add(start)
        while (true) {
            val next = queue.remove()
            val nextValue = field[next]
            if (next == to) return nextValue
            next.neighbors4().forEach { c ->
                val risk = risks[c]
                if (risk < 10 && nextValue + risk < field[c]) {
                    field[c] = nextValue + risk
                    queue.add(c)
                }
            }
        }
    }

    override fun a() = traverse(data)

    override fun b() = traverse(C2IntField(data.width * 5, data.height * 5, 10).also { field ->
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                data.forEach { c, v ->
                    field[C2(c.x + data.width * x, c.y + data.height * y)] = ((v - 1 + x + y) % 9) + 1
                }
            }
        }
    })
}
