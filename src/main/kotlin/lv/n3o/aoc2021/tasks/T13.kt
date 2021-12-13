package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task
import lv.n3o.aoc2021.coords.C2
import lv.n3o.aoc2021.ocr.recognize4X6
import kotlin.math.abs

class T13(input: Input) : Task(input) {
    private val data = input.asLinesPerBlock()
    private val coords = data.first().map { it.split(",").map(String::toInt).let { (x, y) -> C2(x, y) } }.toSet()
    private val folds = data.last().map { line -> line.split("=").let { (it[0].last() == 'x') to it.last().toInt() } }

    private fun fold(dots: Set<C2>, horizontal: Boolean, c: Int): Set<C2> = if (horizontal) {
        dots.map { C2(c - abs(it.x - c), it.y) }
    } else {
        dots.map { C2(it.x, c - abs(it.y - c)) }
    }.toSet()

    override fun a() = folds.first().let { (horizontal, c) -> fold(coords, horizontal, c) }.size

    override fun b() = folds.fold(coords) { sheet, (horizontal, c) -> fold(sheet, horizontal, c) }.recognize4X6()
}