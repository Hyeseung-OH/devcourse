package day1.exam22

fun main() {
    val name: String? = "hello"

    val len = name?.length ?: 0
    println(len)

    val rst = name?.let {
        println(it.length)
        it
    }

    println(rst)

}