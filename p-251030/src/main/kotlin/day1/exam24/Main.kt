package day1.exam24

class Person(
    var name: String,
    var age: Int,
    var weight: Double,
    var salary: Int
) {

    fun increaseAge(years: Int) {
        age += years
    }

    fun increaseWeight(kg: Double) {
        weight += kg
    }

    fun increaseSalary(amount: Int) {
        salary += amount
    }

    fun getInfo() {
        println("이름: $name, 나이: $age, 몸무게: $weight, 연봉: $salary")
    }
}

fun main() {

    val p1 = Person("Alice", 25, 55.5, 3000)

    with(p1) {
        increaseAge(5)
        increaseWeight(3.5)
        increaseSalary(10000)
    }

    p1.getInfo()

}