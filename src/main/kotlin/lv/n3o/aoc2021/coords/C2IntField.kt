package lv.n3o.aoc2021.coords

class C2IntField(val width: Int, val height: Int, defaultValue: Int = 0) {
    constructor(max: C2, defaultValue: Int = 0) : this(max.x + 1, max.y + 1, defaultValue)

    var outOfBoundsValue = defaultValue
    private val max = C2(width - 1, height - 1)
    private val array = IntArray(width * height) { defaultValue }

    operator fun get(c: C2) = c.arrayIndex(max)?.let { array[it] } ?: outOfBoundsValue
    operator fun set(c: C2, value: Int) = c.arrayIndex(max)?.let { array[it] = value }

    fun forEach(f: (C2, Int) -> Unit) {
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                val c = C2(x, y)
                f(c, get(c))
            }
        }
    }
}


fun Map<C2, Int>.toIntField(defaultValue: Int) =
    C2IntField(C2(keys.maxOf { it.x }, keys.maxOf { it.y }), defaultValue).also {
        this.forEach { (c, i) -> it[c] = i }
    }
