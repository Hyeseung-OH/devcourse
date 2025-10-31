package day1.exam10

fun main() {

    val names = listOf("Alice", "Bob", "Charlie")

    names.forEach {name -> println(name)} // 매개변수 표현
    names.forEach {println(it)} // 매개변수 it으로 대체

}