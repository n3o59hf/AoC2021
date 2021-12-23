package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C2
import kotlin.math.min

class T23(input: Input) : Task(input) {
    private val dataA = input.asCoordGrid(false).filter { (_, r) -> r in listOf('.', 'A', 'B', 'C', 'D') }
    private val dataB = dataA.mapKeys { (c) -> if (c.y > 2) C2(c.x, c.y + 2) else c } + mapOf(
        C2(3, 3) to 'D', C2(5, 3) to 'C', C2(7, 3) to 'B', C2(9, 3) to 'A',
        C2(3, 4) to 'D', C2(5, 4) to 'B', C2(7, 4) to 'A', C2(9, 4) to 'C',
    )

    private val exchangeSpots = arrayOf(
        C2(1, 1), C2(2, 1), C2(4, 1), C2(6, 1), C2(8, 1), C2(10, 1), C2(11, 1)
    )

    private val Char.costPerStep
        get() = when (this) {
            'A' -> 1
            'B' -> 10
            'C' -> 100
            'D' -> 1000
            else -> error("Unknown")
        }

    private val Char.preferedColumn get() = 3 + 2 * (this - 'A')
    private val C2.inRoom get() = (x == 3 || x == 5 || x == 7 || x == 9) && y > 1

    inner class Field(input: Map<C2, Char>) {
        val roomSize = (input.keys.maxOfOrNull { it.y } ?: error("Field corrupted")) - 1
        val storage = CharArray(7 + 4 * roomSize) { ' ' }

        init {
            input.forEach { (c, p) ->
                this[c] = p
            }
        }

        fun getSignature() = storage.fold(0L) { acc, c ->
            if (c.code < 64) acc.shl(1)
            else acc.shl(3) + (c.code - 1 and 3) + 4
        }

        operator fun get(c: C2) = when (c.y) {
            1 -> when (c.x) {
                1, 2, 4, 6, 8, 10 -> storage[c.x / 2]
                11 -> storage[6]
                3, 5, 7, 9 -> '.'
                else -> ' '
            }
            in 2 until (2 + roomSize) -> if (c.inRoom) storage[7 + (c.y - 2) * 4 + ((c.x - 3) / 2)] else ' '
            else -> ' '
        }

        operator fun set(c: C2, value: Char) {
            val index = when (c.y) {
                1 -> when (c.x) {
                    1, 2, 4, 6, 8, 10 -> c.x / 2
                    11 -> 6
                    else -> if (c.x in 1..11 && value == '.') return else error("Out of bounds $c")
                }
                in 2..(2 + roomSize) -> {
                    val roomShift = (c.x - 3) % 2
                    val roomColumn = (c.x - 3) / 2
                    val roomRow = c.y - 2
                    if (roomShift == 0 && roomColumn in (0 until 4) && roomRow < roomSize) 7 + roomRow * 4 + roomColumn
                    else error("Out of bounds $c")
                }
                else -> error("Out of bounds $c")
            }
            storage[index] = value
        }

        inline fun withSwapped(a: C2, b: C2, f: () -> Unit) {
            val tmp = this[a]
            this[a] = this[b]
            this[b] = tmp
            f()
            this[b] = this[a]
            this[a] = tmp
        }

        inline fun withSet(a: C2, char: Char, f: () -> Unit) {
            val tmp = this[a]
            this[a] = char
            f()
            this[a] = tmp
        }

        fun hasRoute(a: C2, b: C2): Boolean {
            if (this[b] != '.') return false
            var current = a
            while (current != b) {
                val next = current.neighbors4().filter { this[it] == '.' }.sortedBy { it.y }
                    .firstOrNull { it.distance(b) < current.distance(b) } ?: return false
                current = next
            }
            return true
        }
    }

    private fun solve(initialSetup: Map<C2, Char>): Int {
        val cache = mutableMapOf<Long, Int>()
        var setup = initialSetup
        while (setup.any { (c, p) -> p.preferedColumn == c.x && setup[c.down] == null }) {
            setup = setup.filterNot { (c, p) -> p.preferedColumn == c.x && setup[c.down] == null }
        }
        val field = Field(setup)

        fun iteration(toPlace: List<C2>): Int {
            if (toPlace.isEmpty()) return 0
            var minCost = Int.MAX_VALUE

            toPlace.forEach { c ->
                val current = field[c]
                if (c.inRoom) {
                    if (field.hasRoute(c, C2(c.x, 1))) {
                        exchangeSpots.asSequence().filter { field.hasRoute(c, it) }.forEach { e ->
                            field.withSwapped(c, e) {
                                val cost = cache[field.getSignature()] ?: iteration(toPlace - c + e)
                                if (cost != Integer.MAX_VALUE) {
                                    minCost = min(minCost, current.costPerStep * c.distance(e) + cost)
                                }
                            }
                        }
                    }
                } else {
                    var preferredSpot = C2(current.preferedColumn, 1)
                    while (field[preferredSpot.down] == '.') preferredSpot = preferredSpot.down
                    if (field[preferredSpot.down] == ' ' && preferredSpot.y > 1 && field.hasRoute(c, preferredSpot)) {
                        field.withSet(c, '.') {
                            field.withSet(preferredSpot, ' ') {
                                val cost = cache[field.getSignature()] ?: iteration(toPlace - c)
                                if (cost != Integer.MAX_VALUE) {
                                    minCost = min(minCost, current.costPerStep * c.distance(preferredSpot) + cost)
                                }
                            }
                        }
                    }
                }
            }
            cache[field.getSignature()] = minCost
            return minCost
        }

        return iteration(setup.filter { it.value != '.' }.keys.toList())
    }


    override fun a() = solve(dataA)

    override fun b() = solve(dataB)
}