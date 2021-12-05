package lv.n3o.aoc2021.coords

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

data class C2(val x: Int, val y: Int) : Comparable<C2> {
    operator fun plus(other: C2) = C2(x + other.x, y + other.y)
    operator fun minus(other: C2) = C2(x - other.x, y - other.y)
    operator fun times(other: Int) = C2(x * other, y * other)
    operator fun div(other: Int): C2 = C2(x / other, y / other)

    fun distance(to: C2) = abs(to.x - x) + abs(to.y - y)

    fun vector(to: C2) = C2(to.x - x, to.y - y)

    fun clockAngle(to: C2): Double {
        val vector = vector(to)
        return when {
            vector.x == 0 && vector.y < 0 -> 0.0
            vector.x == 0 && vector.y > 0 -> 0.5
            vector.y == 0 && vector.x > 0 -> 0.25
            vector.y == 0 && vector.x < 0 -> 0.75
            else -> {
                var clockDial = 0.5 - atan2(vector.x.toDouble(), vector.y.toDouble()) / (2 * PI)
                if (clockDial >= 1.0) clockDial -= 1.0
                if (clockDial < 0) clockDial += 1.0
                clockDial
            }
        }
    }

    fun rotate(direction: Boolean) = if (direction) rotateRight() else rotateLeft()

    fun rotateRight() = C2(-y, x)
    fun rotateLeft() = C2(y, -x)

    fun unit() = C2(
        x.coerceIn(-1, 1),
        y.coerceIn(-1, 1)
    )

    fun neighbors4() = listOf(
        this + C2(0, -1),
        this + C2(1, 0),
        this + C2(0, 1),
        this + C2(-1, 0)
    )


    override fun equals(other: Any?): Boolean {
        if (other !is C2) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "C2($x, $y)"
    }

    override fun compareTo(other: C2) = x.compareTo(other.x).takeIf { it != 0 } ?: y.compareTo(other.y)
}
