package lv.n3o.aoc2021

import lv.n3o.aoc2021.coords.C2

typealias DebugListener = (String) -> Unit

abstract class Input {
    private val input: String by lazy { readInput() }
    abstract fun readInput(): String

    fun raw() = input

    fun asLines(trim: Boolean = true): List<String> = input.split("\n").filter { it.isNotBlank() }.let {
        if (trim) it.map(String::trim) else it
    }

    fun asListOfLongs() = asLines().map {
        it.trim().toLong()
    }

    fun asListOfInts() = asLines().map {
        it.trim().toInt()
    }

    fun asCoordGrid(): Map<C2, Char> = asLines().flatMapIndexed { y, l ->
        l.mapIndexed { x, c ->
            C2(x, y) to c
        }
    }.toMap()

    fun asLinesPerBlock(trim: Boolean = true): List<List<String>> = input.replace("\r", "").split("\n\n").map {
            it.split("\n").let { block ->
                if (trim) block.map(String::trim) else block
            }
        }
}

class ClasspathInput(val name: String) : Input() {
    override fun readInput(): String {
        with(this::class.java.classLoader.getResourceAsStream(name)!!) {
            return bufferedReader(Charsets.UTF_8).readText()
        }
    }
}

abstract class Task(val input: Input) {
    var debugListener: DebugListener? = null

    fun isLoggerOn() = debugListener != null
    fun log(vararg things: Any?) {
        debugListener ?: return

        val logline = things.map { it ?: "<null>" }.joinToString(" ") { it.toString() }
        val functionName = Thread.currentThread().stackTrace[2].methodName.padStart(12, ' ')
        val time = timeFromApplicationStart.formatTime()
        val lineNumber = Thread.currentThread().stackTrace[2].lineNumber.toString().padStart(5, ' ')

        debugListener?.let { it(("$functionName:$lineNumber: ($time) $logline")) }
    }

    fun log(scope: () -> String) {
        if (isLoggerOn()) log(scope())
    }

    open fun a(): String = ""
    open fun b(): String = ""
}