package lv.n3o.aoc2021.coords

import kotlin.math.abs

private val NEIGHBORS81 = (-1..1).flatMap { dx ->
    (-1..1).flatMap { dy ->
        (-1..1).flatMap { dz ->
            (-1..1).mapNotNull { dw ->
                if (dx == 0 && dy == 0 && dz == 0 && dw == 0) null else C4(dx, dy, dz, dw)
            }
        }
    }
}.toSet()

data class C4(
    val x: Int, val y: Int, val z: Int, val w: Int
) {
    val neighbors81 by lazy { NEIGHBORS81.map { this + it } }

    operator fun plus(other: C4) = C4(x + other.x, y + other.y, z + other.z, w + other.w)
    operator fun minus(other: C4) = this + C4(-other.x, -other.y, -other.z, -other.w)

    fun distance(to: C4) = abs(to.x - x) + abs(to.y - y) + abs(to.z - z) + abs(to.w - w)

    fun vector(to: C4) = C4(to.x - x, to.y - y, to.z - z, to.w - w)
    fun unit() = C4(
        when {
            x > 0 -> 1
            x < 0 -> -1
            else -> 0
        }, when {
            y > 0 -> 1
            y < 0 -> -1
            else -> 0
        }, when {
            z > 0 -> 1
            z < 0 -> -1
            else -> 0
        }, when {
            w > 0 -> 1
            w < 0 -> -1
            else -> 0
        }
    )
}