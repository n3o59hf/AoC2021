package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C3

class T19(input: Input) : Task(input) {
    private val scanners = input.asLinesPerBlock().map { block ->
        block.drop(1).map { line -> line.split(",").map(String::toInt).let { (x, y, z) -> C3(x, y, z) } }
    }.map { p -> Scanner(p) }.let { scanners ->
        val unalignedScanners = scanners.toMutableList()
        val alignedScanners = mutableListOf(unalignedScanners.removeAt(0))
        for (i in scanners.indices) {
            val currentScanner = alignedScanners[i]
            val iterator = unalignedScanners.listIterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.alignTo(currentScanner)) {
                    alignedScanners.add(next)
                    iterator.remove()
                }
            }
        }
        alignedScanners.toList()
    }

    private class Scanner(val probes: List<C3>) {
        var position = C3(0, 0, 0)
        var correctedProbes = probes

        fun alignTo(other: Scanner): Boolean {
            val aligned = other.correctedProbes
            for (rotationIndex in 0 until 24) {
                val differencesCount = mutableMapOf<C3, Int>()
                probes.asSequence().map { it.rotations[rotationIndex] }
                    .map { our -> aligned.map { other -> other - our } }.flatten().forEach { d ->
                        val matches = (differencesCount[d] ?: 0) + 1
                        if (matches == 12) {
                            position = d
                            correctedProbes = probes.map { it.rotations[rotationIndex] + position }
                            return true
                        }
                        differencesCount[d] = matches
                    }
            }
            return false
        }
    }

    override fun a() = scanners.flatMap { it.correctedProbes }.toSet().size

    override fun b() = scanners.maxOf { a -> scanners.maxOf { b -> a.position.distance(b.position) } }
}

