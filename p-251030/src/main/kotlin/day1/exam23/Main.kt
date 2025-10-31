package day1.exam23

fun main() {

    val num = 5

    // apply => this를 받고 객체를 반환
    // let => it을 받고 람다의 결과값 반환
    // also => it을 받고 객체를 반환
    // run => this를 받고 람다 값을 반환

    val rst = num.run {
        this * 2 + 10
    }

//    val rst = num * 2 + 10

    println(rst)

}