package day1.exam7

import kotlin.collections.iterator

fun main() {
    val ages = mapOf("Peter" to 24, "Clark" to 31, "Bruce" to 32)

//    ages.put("Barry", 25)

    for ((key, value) in ages) {
        println("$key is $value years old.")
    }

}