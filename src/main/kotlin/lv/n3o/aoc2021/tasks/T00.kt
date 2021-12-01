package lv.n3o.aoc2021.tasks

import lv.n3o.aoc2021.Input
import lv.n3o.aoc2021.Task

class T00(input: Input) : Task(input) {
    val data = input.raw()

    override fun a(): String {
        return data
    }

    override fun b(): String {
        return data.length.toString()
    }

}