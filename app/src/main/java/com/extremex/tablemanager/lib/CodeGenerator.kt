package com.extremex.tablemanager.lib
import kotlin.random.Random

class CodeGenerator {

    private val letters = ('A'..'Z').toList()
    private val numbers = ('0'..'9').toList()

    fun generateCode(length: Int = 10): String {
        if (length <= 0) throw IllegalArgumentException("Length should be greater than 0")

        return StringBuilder(length).apply {
            repeat(length) {
                when (Random.nextBoolean()) {
                    true -> append(randomLetter())
                    false -> append(randomNumber())
                }
            }
        }.toString()
    }

    private fun randomLetter(): Char {
        return letters[Random.nextInt(letters.size)]
    }

    private fun randomNumber(): Char {
        return numbers[Random.nextInt(numbers.size)]
    }
}