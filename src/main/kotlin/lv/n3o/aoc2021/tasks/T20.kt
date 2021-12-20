package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C2
import lv.n3o.aoc2021.get

class T20(input: Input) : Task(input) {
    private val data = input.asBlocks()
    private val decoder = data.first().raw().toList().map { it == '#' }
    private val initialState = data.last().asCoordGrid().filterValues { it == '#' }.keys.let { pixels ->
        val size = pixels.maxOf { it.x.coerceAtLeast(it.y) } + 1
        Image(BooleanArray(size * size) { i -> pixels.contains(C2(i % size, i / size)) }, size)
    }

    inner class Image(val pixels: BooleanArray, val size: Int, val borderPixel: Boolean = false) {
        val pixelsOn get() = if (borderPixel) Int.MAX_VALUE else pixels.count { it }
        fun enhance() = Image(
            BooleanArray((size + 2) * (size + 2)) { c ->
                val x = (c % (size + 2)) - 1
                val y = (c / (size + 2)) - 1
                var index = 0
                for (j in y + -1..y + 1) {
                    for (i in x - 1..x + 1) {
                        index = index.shl(1)
                        if (if (j < 0 || j >= size || i < 0 || i >= size) borderPixel else pixels[i + j * size]) {
                            index++
                        }
                    }
                }
                decoder[index]
            }, size + 2, if (borderPixel) decoder[0b111111111] else decoder[0]
        )
    }

    private val enhancements = generateSequence(initialState) { it.enhance() }

    override fun a() = enhancements[2].pixelsOn

    override fun b() = enhancements[50].pixelsOn
}