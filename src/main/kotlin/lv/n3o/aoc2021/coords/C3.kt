package lv.n3o.aoc2021.coords

import kotlin.math.abs

data class C3(
    val x: Int, val y: Int, val z: Int
) : Comparable<C3> {
    val rotations by lazy {
        listOf(
            this,
            C3(x = y, y = -x, z = z),
            C3(x = -x, y = -y, z = z),
            C3(x = -y, y = x, z = z),
            C3(x = -y, y = -z, z = x),
            C3(x = -z, y = y, z = x),
            C3(x = y, y = z, z = x),
            C3(x = z, y = -y, z = x),
            C3(x = z, y = -x, z = -y),
            C3(x = -x, y = -z, z = -y),
            C3(x = -z, y = x, z = -y),
            C3(x = x, y = z, z = -y),
            C3(x = -z, y = -y, z = -x),
            C3(x = -y, y = z, z = -x),
            C3(x = z, y = y, z = -x),
            C3(x = y, y = -z, z = -x),
            C3(x = y, y = x, z = -z),
            C3(x = x, y = -y, z = -z),
            C3(x = -y, y = -x, z = -z),
            C3(x = -x, y = y, z = -z),
            C3(x = -x, y = z, z = y),
            C3(x = z, y = x, z = y),
            C3(x = x, y = -z, z = y),
            C3(x = -z, y = -x, z = y)
        )
    }

    operator fun plus(other: C3) = C3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: C3) = this + C3(-other.x, -other.y, -other.z)

    fun distance(to: C3) = abs(to.x - x) + abs(to.y - y) + abs(to.z - z)

    fun vector(to: C3) = C3(to.x - x, to.y - y, to.z - z)
    fun unit() = C3(
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
        }
    )

    fun neighbors27(includeSelf: Boolean = false) = (-1..1).flatMap { dx ->
        (-1..1).flatMap { dy ->
            (-1..1).mapNotNull { dz ->
                if (!includeSelf && dx == 0 && dy == 0 && dz == 0) null else this + C3(dx, dy, dz)
            }
        }
    }

    fun inBoxInclusive(from: C3, to: C3) =
        this.x >= from.x && this.y >= from.y && this.z >= from.z && this.x <= to.x && this.y <= to.y && this.z <= to.z

    fun vectorVolume() = x * y * z
    fun vectorVolumeLong() = x.toLong() * y.toLong() * z.toLong()

    override fun compareTo(other: C3) =
        x.compareTo(other.x).takeIf { it != 0 } ?: y.compareTo(other.y).takeIf { it != 0 } ?: z.compareTo(other.z)
}