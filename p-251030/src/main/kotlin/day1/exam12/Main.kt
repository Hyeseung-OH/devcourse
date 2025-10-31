package day1.exam12

// String 클래스에 greet 확장 함수 추가
fun String.greet() {
    println("hello, $this ~!")
}

// Int List 클래스에 square 확장 함수 추가
fun List<Int>.square(): List<Int> {
    val rst = this.map { it * it }
    return rst
}

fun main() {
    val name: String = "Alice"
    name.greet() // hello, Alice ~!


    val intList = listOf(1, 2, 3, 4, 5)

    val rst = intList.average()
    println(rst)

    val squaredIntList = intList.square()
    println("===========================")
    squaredIntList.forEach(::println)

}