package day1.exam14

class Test {

}

object Singleton {
    fun showMessage() {
        println("Hello, World!")
    }
}

fun main() {
    val a = Singleton
    val b = Singleton

    println(a == b) // Singleton 이므로 항상 같은 인스턴스가 반환됨.

    val t1 = Test()
    val t2 = Test()

    println(t1 == t2) // 객체 생성마다 새로운 인스턴스가 생성되므로 서로 다른 인스턴스임.
}