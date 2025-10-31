package day1.exam26

val lazyValue: String by lazy {
    println("initializing")
    "Hello"
}

fun main() {

    println("before accessing lazyValue")
    println(lazyValue)
    println(lazyValue)
}