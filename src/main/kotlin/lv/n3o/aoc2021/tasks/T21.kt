package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T21(input: Input) : Task(input) {
    private val positions = input.asLines().map { it.split(": ").last().toInt() }
    val p1StartPosition = positions.first() - 1
    val p2StartPosition = positions.last() - 1

    inner class GameState(
        val p1Pos: Int = p1StartPosition,
        val p1Score: Int = 0,
        val p2Pos: Int = p2StartPosition,
        val p2Score: Int = 0,
        val p1Turn: Boolean = true
    ) {
        fun rollDice(points: Int) = if (p1Turn) {
            val p1NewPos = (p1Pos + points) % 10
            GameState(p1NewPos, p1Score + p1NewPos + 1, p2Pos, p2Score, false)
        } else {
            val p2NewPos = (p2Pos + points) % 10
            GameState(p1Pos, p1Score, p2NewPos, p2Score + p2NewPos + 1, true)
        }

        override fun equals(other: Any?) =
            other is GameState && p1Pos == other.p1Pos && p1Score == other.p1Score && p2Pos == other.p2Pos && p2Score == other.p2Score && p1Turn == other.p1Turn

        override fun hashCode() = ((((p1Turn.hashCode() * 31) + p1Pos) * 31 + p1Score) * 31 + p2Pos) * 31 + p2Score
    }

    override fun a() = sequence {
        val dice = generateSequence(1) { (it % 100) + 1 }.windowed(3, 3).map { it.sum() }
        var state = GameState()
        for (roll in dice) {
            yield(state)
            state = state.rollDice(roll)
        }
    }.withIndex().first { (_, state) -> state.p1Score >= 1000 || state.p2Score >= 1000 }.let { (i, state) ->
        i * 3 * if (state.p1Score > state.p2Score) state.p2Score else state.p1Score
    }


    override fun b(): Long {
        val diceRolls = listOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
        val cache = mutableMapOf<GameState, Pair<Long, Long>>()

        fun quantumTurn(gameState: GameState): Pair<Long, Long> {
            cache[gameState]?.let { return it }
            if (gameState.p1Score >= 21) return 1L to 0L
            if (gameState.p2Score >= 21) return 0L to 1L
            var wins = 0L to 0L
            for ((dice, multiplier) in diceRolls) {
                val newWins = quantumTurn(gameState.rollDice(dice))
                wins = (wins.first + newWins.first * multiplier) to (wins.second + newWins.second * multiplier)
            }

            cache[gameState] = wins
            return wins
        }

        return quantumTurn(GameState()).toList().maxOrNull() ?: error("Pair has no elements?")
    }
}