package day1.exam20

fun main() {

    val obj: Any = "Hello"

    if(obj is String) {
        println(obj.length)
    }

}