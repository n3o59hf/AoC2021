package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T16(input: Input) : Task(input) {
    val packet: Packet

    init {
        val data = input.raw().flatMap { c ->
            c.digitToInt(16).toString(2).padStart(4, '0').map(Char::digitToInt)
        }
        var position = 0

        fun readPackets(maxCount: Int = Int.MAX_VALUE, maxLimit: Int = data.size): List<Packet> {
            fun read(bits: Int) = (0 until bits).fold(0) { acc, _ -> acc.shl(1) + data[position++] }

            val packets = mutableListOf<Packet>()

            while (position < maxLimit && packets.size < maxCount) {
                val version = read(3)
                packets.add(
                    when (val type = read(3)) {
                        4 -> {
                            var literal = 0L
                            do {
                                val part = read(5)
                                literal = literal.shl(4) + (part and 0xF)
                            } while (part > 0xF)
                            Packet.LiteralPacket(version, literal)
                        }
                        else -> Packet.OperatorPacket(
                            version, type, if (read(1) == 0) {
                                readPackets(maxLimit = read(15) + position)
                            } else {
                                readPackets(maxCount = read(11))
                            }
                        )
                    }
                )
            }
            return packets
        }

        packet = readPackets(1).first()
    }

    sealed class Packet {
        abstract val versionSum: Int
        abstract val result: Long

        class LiteralPacket(override val versionSum: Int, override val result: Long) : Packet()
        class OperatorPacket(version: Int, type: Int, subpackets: List<Packet>) : Packet() {
            override val versionSum = version + subpackets.sumOf { it.versionSum }
            override val result: Long = when (type) {
                0 -> subpackets.sumOf { it.result }
                1 -> subpackets.fold(1L) { acc, p -> acc * p.result }
                2 -> subpackets.minOf { it.result }
                3 -> subpackets.maxOf { it.result }
                5 -> if (subpackets[0].result > subpackets[1].result) 1 else 0
                6 -> if (subpackets[0].result < subpackets[1].result) 1 else 0
                7 -> if (subpackets[0].result == subpackets[1].result) 1 else 0
                else -> error("Unsupported")
            }
        }
    }

    override fun a() = packet.versionSum
    override fun b() = packet.result
}