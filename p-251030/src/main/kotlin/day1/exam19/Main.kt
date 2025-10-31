package day1.exam19

fun main() {
    val names = listOf("Alice", "Bob", "Charlie")

    names.map {
        "Hello, $it"
    }
        .forEach(::println)

}