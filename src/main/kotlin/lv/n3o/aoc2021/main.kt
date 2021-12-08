@file:JvmName("Main")

package lv.n3o.aoc2021

import kotlinx.coroutines.runBlocking
import kotlin.math.min
import kotlin.system.measureTimeMillis

private const val FIRST_DAY = 1
private const val LAST_DAY = 25
private const val ANSWER_SIZE = 48
private const val TIME_SIZE = 10
private const val TEST_NEW_TASKS = true
private const val REPEAT_RUNS = 1

private fun ci(number: Int, expectedA: String, expectedB: String): TestCase {
    val paddedNumber = "$number".padStart(2, '0')
    val className = "T$paddedNumber"
    val inputFile = "i$paddedNumber.txt"
    val taskClass = Class.forName("lv.n3o.aoc2021.tasks.$className")
    val constructor: (Input) -> Task =
        { input -> taskClass.getConstructor(Input::class.java).newInstance(input) as Task }


    return TestCase(
        "T:$paddedNumber", ClasspathInput(inputFile), constructor, expectedA, expectedB
    )
}

val testCases: List<TestCase> = listOf(
    ci(1, "1713", "1734"),
    ci(2, "1580000", "1251263225"),
    ci(3, "2640986", "6822109"),
    ci(4, "8136", "12738"),
    ci(5, "5835", "17013"),
    ci(6, "356190", "1617359101538"),
    ci(7, "340987", "96987874"),
    ci(8, "534", "1070188"),
)

fun main() = runBlocking {
    //If tasks not registered are present, execute first such task
    if (TEST_NEW_TASKS) if (testNewTasks()) return@runBlocking
    var minTime = Long.MAX_VALUE
    (1..REPEAT_RUNS).forEach { runId ->
        val time = measureTimeMillis {
            val results = testCases.asSequence().map(::executeTask)


            println()
            printTableSeparator()
            println(
                "| TASK | ${"Answer A".padCenter(ANSWER_SIZE)} | ${"Answer B".padCenter(ANSWER_SIZE)} | ${
                    "Time".padCenter(
                        TIME_SIZE
                    )
                } |"
            )
            printTableSeparator()

            results.forEach { result ->
                print("|")
                print(result.name.padCenter(6, ' '))

                print("| ")
                print(if (result.correctA) "OK" else "F ")
                print((result.resultA ?: "").padStart(ANSWER_SIZE - 2, ' '))
                print(" | ")
                print(if (result.correctB) "OK" else "F ")
                print((result.resultB ?: "").padStart(ANSWER_SIZE - 2, ' '))
                print(" | ")
                print(result.time.toString().padStart(TIME_SIZE, ' '))
                println(" |")


                if (!result.correctA) {
                    println("A: Correct=${result.expectedA}")
                }
                if (result.exceptionA != null) {
                    println("A: Exception=${result.exceptionA}")
                }
                if (!result.correctB) {
                    println("B: Correct=${result.expectedB}")
                }
                if (result.exceptionB != null) {
                    println("B: Exception=${result.exceptionB}")
                }

            }
            printTableSeparator()
        }
        minTime = min(minTime, time)
        if (REPEAT_RUNS != 1) {
            println("RUN ${runId.toString().padStart(5, '0')}\tExecution time: $time")
        } else {
            println("Execution time: $time")
        }
        println()
    }
    if (REPEAT_RUNS != 1) {
        println("Fastest run: $minTime")
        println()
    }
}

fun executeTask(tc: TestCase): TestResult {
    val timeStart = System.currentTimeMillis()

    val task = tc.executor(tc.input)

    val (answerA, exceptionA) = try {
        task.a() to null
    } catch (e: Exception) {
        null to e
    }


    val (answerB, exceptionB) = try {
        task.b() to null
    } catch (e: Exception) {
        null to e
    }

    val timeEnd = System.currentTimeMillis()
    return TestResult(
        tc.name,
        tc.input,
        tc.expectedA == answerA,
        answerA,
        exceptionA,
        tc.expectedA,
        tc.expectedB == answerB,
        answerB,
        exceptionB,
        tc.expectedB,
        timeEnd - timeStart
    )
}

fun testNewTasks(): Boolean {
    (FIRST_DAY..LAST_DAY).forEach { i ->
        try {
            val c = ci(i, "", "")
            if (testCases.none { it.name == c.name }) {
                println("Executing ${c.name}")
                val time = measureTimeMillis {
                    val task = c.executor(c.input)
                    task.debugListener = { s ->
                        println(s)
                    }
                    println(task.a())
                    println(task.b())
                }
                println("Time: $time")
                return true
            }
        } catch (ignored: ClassNotFoundException) {
        }
    }
    return false
}

fun printTableSeparator() {
    print("|")
    print("-".repeat(6))
    print("+")
    print("-".repeat(ANSWER_SIZE + 2))
    print("+")
    print("-".repeat(ANSWER_SIZE + 2))
    print("+")
    print("-".repeat(TIME_SIZE + 2))
    println("|")
}

class TestCase(
    val name: String,
    val input: Input,
    val executor: (Input) -> Task,
    val expectedA: String,
    val expectedB: String,
)

class TestResult(
    val name: String,
    val input: Input,
    val correctA: Boolean,
    val resultA: String?,
    val exceptionA: Throwable?,
    val expectedA: String,
    val correctB: Boolean,
    val resultB: String?,
    val exceptionB: Throwable?,
    val expectedB: String,
    val time: Long,
) {
    val correct = correctA && correctB
}


fun String.padCenter(targetLength: Int, char: Char = ' ') = if (this.length < targetLength) {
    val difference = targetLength - this.length
    val start = difference / 2
    this.padStart(start + this.length, char).padEnd(targetLength, char)
} else this