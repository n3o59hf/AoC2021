package lv.n3o.aoc2021.coords

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

data class C2(val x: Int, val y: Int) : Comparable<C2> {
    constructor(index: Int, max: C2) : this(index % (max.x + 1), index / (max.x + 1))

    val down get() = this + DIRECTION_DOWN
    val up get() = this + DIRECTION_UP
    val right get() = this + DIRECTION_RIGHT
    val left get() = this + DIRECTION_LEFT

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
        x.coerceIn(-1, 1), y.coerceIn(-1, 1)
    )

    fun neighbors4() = listOf(up, right, down, left)

    fun neighbors8() = listOf(
        up, right, down, left, this + C2(1, -1), this + C2(1, 1), this + C2(-1, 1), this + C2(-1, -1)
    )

    fun arrayIndex(max: C2) = if (x < 0 || y < 0 || x > max.x || y > max.y) null else x + y * (max.x + 1)

    fun inSquareInclusive(leftTop: C2, bottomRight: C2) =
        x >= leftTop.x && x <= bottomRight.x && y >= leftTop.y && y <= bottomRight.y

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

    companion object {
        val DIRECTION_DOWN = C2(0, 1)
        val DIRECTION_UP = C2(0, -1)
        val DIRECTION_RIGHT = C2(1, 0)
        val DIRECTION_LEFT = C2(-1, 0)
    }
}
