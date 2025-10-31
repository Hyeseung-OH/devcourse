package day1.exam21

class Person {
    var name: String = ""
    var age: Int = 0

    fun printInfo() {
        println("$name is $age years old.")
    }
}

fun main() {
    val person = Person().apply {
        age = 30
        name = "Alice"
    }

    person.printInfo()
}