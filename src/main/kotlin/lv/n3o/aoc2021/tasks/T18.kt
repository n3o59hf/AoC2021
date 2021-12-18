package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T18(input: Input) : Task(input) {
    private val data = input.asLines().map { getNumber(it.iterator()) }

    private fun getNumber(stream: Iterator<Char>): SnailNumber = when (val next = stream.next()) {
        '[' -> {
            val a = getNumber(stream)
            stream.next() // ,
            val b = getNumber(stream)
            stream.next() // ]
            SnailNumber.Pair(a, b)
        }
        else -> SnailNumber.Value(next.digitToInt())
    }

    private sealed class SnailNumber(var parent: Pair? = null) {
        abstract val magnitude: Int

        class Value(override var magnitude: Int) : SnailNumber() {
            override fun split() = if (magnitude > 9) {
                parent?.replace(this, Pair(Value(magnitude / 2), Value(magnitude / 2 + magnitude % 2)))
                true
            } else false

            override fun explode() = false
            override fun copy() = Value(magnitude)
            override fun toString() = magnitude.toString()
        }

        class Pair(var left: SnailNumber, var right: SnailNumber) : SnailNumber() {
            init {
                right.parent = this
                left.parent = this
            }

            override val magnitude get() = left.magnitude * 3 + right.magnitude * 2
            private val depth: Int get() = (parent?.depth ?: 0) + 1
            val valueToLeft get() = neighbor(false)
            val valueToRight get() = neighbor(true)

            private fun neighbor(right: Boolean): Value? {
                var from: SnailNumber = this
                var current: SnailNumber? = this.parent
                while (current is Pair && (if (right) current.right else current.left) == from) {
                    from = current
                    current = current.parent
                }
                current = (current as? Pair)?.let { if (right) it.right else it.left }
                while (current is Pair) current = if (right) current.left else current.right
                return current as? Value
            }

            fun replace(old: SnailNumber, new: SnailNumber) {
                when {
                    left === old -> left = new
                    right === old -> right = new
                    else -> error("Child not found $old, children: $left, $right")
                }
                new.parent = this
            }

            override fun explode(): Boolean = if (right is Value && left is Value && depth > 4) {
                valueToLeft?.let { c -> c.parent?.replace(c, Value(c.magnitude + left.magnitude)) }
                valueToRight?.let { c -> c.parent?.replace(c, Value(c.magnitude + right.magnitude)) }
                parent?.replace(this, Value(0))
                true
            } else {
                left.explode() || right.explode()
            }

            override fun split(): Boolean = left.split() || right.split()
            override fun copy() = Pair(left.copy(), right.copy())
            override fun toString() = "[$left,$right]"
        }

        operator fun plus(other: SnailNumber) = Pair(this.copy(), other.copy()).also { it.normalize() }

        open fun normalize() {
            while (explode() || split()) {
                // Empty
            }
        }

        protected abstract fun explode(): Boolean
        protected abstract fun split(): Boolean
        protected abstract fun copy(): SnailNumber
    }

    override fun a() = data.reduce { a, b -> a + b }.magnitude

    override fun b() = data.maxOf { a -> data.filter { it !== a }.maxOf { b -> (a + b).magnitude } }
}