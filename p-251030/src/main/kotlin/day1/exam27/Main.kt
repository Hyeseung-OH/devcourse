package day1.exam27

class MathUtil {

    companion object {

        val PI = 3.141569

        fun square(n: Int): Int {
            return n * n
        }
    }

}

fun main() {
    MathUtil.square(5).also {
        println(it)
    }

    MathUtil.PI.also {
        println(it)
    }

}