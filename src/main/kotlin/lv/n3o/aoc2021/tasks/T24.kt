package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T24(input: Input) : Task(input) {
    private val program = input.asLines().map {
        val parts = it.split(" ", limit = 2)
        val name = parts.first()
        val arguments = parts.last().split(" ")
        val regA = arguments[0][0] - 'w'
        if (arguments.size == 1) {
            Instruction(name, regA)
        } else {
            val bArgument = arguments[1]
            bArgument.toLongOrNull()?.let { bValue ->
                Instruction(name, regA, argB = bValue)
            } ?: Instruction(name, regA, regB = bArgument[0] - 'w')
        }
    }

    private val limitRange = ((11L..99L)).map { listOf(it / 10, it % 10) }.filterNot { l -> l.any { it == 0L } }

    class Instruction(val name: String, val regA: Int, val regB: Int? = null, val argB: Long? = null) {
        operator fun invoke(registers: LongArray, input: Iterator<Long>): Boolean {
            if (name == "inp") {
                if (!input.hasNext()) return false
                registers[regA] = input.next()
            } else {
                val aValue = registers[regA]
                val bValue = argB ?: regB?.let { registers[regB] } ?: error("$name requires 2 arguments")
                registers[regA] = when (name) {
                    "add" -> aValue + bValue
                    "mul" -> aValue * bValue
                    "div" -> aValue / bValue
                    "mod" -> aValue % bValue
                    "eql" -> if (aValue == bValue) 1 else 0
                    else -> error("Unknown instruction $name with args $regA,$regB/$argB")
                }
            }
            return true
        }
    }

    private fun verifySerial(serial: List<Long>): Long {
        val registers = LongArray(4) { 0L }
        val input = serial.iterator()
        program.forEach { instruction ->
            if (!instruction(registers, input)) return registers[3]
        }
        return registers[3]
    }

    private fun matchAnswer(priority: LongProgression): String {
        fun validPrefix(prefix: List<Long>): Boolean {
            val data = prefix.toMutableList()
            while (data.size < 14) {
                data.add(limitRange.minByOrNull { d -> verifySerial(data + d) }?.first() ?: error("Nothing in range"))
            }
            return verifySerial(data) == 0L
        }
        return (0 until 14).fold(listOf<Long>()) { l, _ -> l + priority.first { validPrefix(l + it) } }.joinToString("")
    }

    override fun a() = matchAnswer(9L downTo 1L)

    override fun b() = matchAnswer(1L..9L)

}