package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.prioritizingComparator

class T12(input: Input) : Task(input) {
    private val connections =
        input.asLines().map { it.split("-") }.flatMap { listOf(it[1] to it[0], it[0] to it[1]) }.toSet()
    private val nodeNames = connections.map { it.first }.toSet()
        .sortedWith(prioritizingComparator(listOf("start", "end"), String::compareTo)).toList()

    private val start = 0
    private val end = 1
    private val allLarge = (end + 1)..nodeNames.withIndex().last { it.value[0].isUpperCase() }.index
    private val allSmall =
        (nodeNames.withIndex().first { it.index > end && it.value[0].isLowerCase() }.index until nodeNames.size)

    private val links = Array(nodeNames.size) { i ->
        connections.filter { it.first == nodeNames[i] }.map { nodeNames.indexOf(it.second) }.toIntArray()
    }

    private fun traverse(
        visited: Array<Int> = Array(links.size) { 0 },
        current: Int = start,
        canTraverseSmall: (Array<Int>, Int) -> Boolean
    ): Int {
        visited[current]++
        val traversed = links[current].sumOf { next ->
            when {
                next == start -> 0
                next == end -> 1
                next in allLarge || canTraverseSmall(visited, next) -> traverse(visited, next, canTraverseSmall)
                else -> 0
            }
        }
        visited[current]--
        return traversed
    }

    override fun a() = traverse { traversed, next -> traversed[next] == 0 }

    override fun b() = traverse { traversed, next -> traversed[next] == 0 || allSmall.all { traversed[it] < 2 } }
}