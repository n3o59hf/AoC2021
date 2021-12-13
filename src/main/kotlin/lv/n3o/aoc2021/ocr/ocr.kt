package lv.n3o.aoc2021.ocr

import lv.n3o.aoc2021.coords.C2

fun Set<C2>.recognize4X6() = (this.minOf(C2::x)..this.maxOf(C2::x)).windowed(4, 5).joinToString("") { columns ->
    columns.reversed().flatMap { x -> (this.minOf(C2::y)..this.minOf(C2::y) + 7).map { y -> contains(C2(x, y)) } }
        .foldRight(0) { bit, acc -> acc.shl(1) + if (bit) 1 else 0 }
        .let { FONT_4X6[it] ?: error("${it.toString(16)} UNKNOWN") }
}
