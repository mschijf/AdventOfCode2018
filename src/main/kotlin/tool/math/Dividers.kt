package tool.math

import kotlin.math.sqrt

fun Int.properDivisors(): Set<Int> {
    val resultSet = mutableSetOf(1)
    for (i in 2 ..sqrt(this.toDouble()).toInt()) {
        if (this % i == 0) {
            resultSet.add(i)
            resultSet.add(this / i)
        }
    }
    return resultSet
}

fun Int.dividerCount(): Int {
    var count = 1
    for (i in 2 ..sqrt(this.toDouble()).toInt()) {
        if (this % i == 0)
            count++
    }
    return 2*count
//    return properDivisors().count()+1
}

fun Int.sumOfProperDivisors(): Int {
    var sum = 1
    for (i in 2 .. sqrt(this.toDouble()).toInt()) {
        if (this % i == 0) {
            val other = this/i
            sum += (i + if (i != other) other  else 0)
        }
    }
    return sum
//    return properDivisors().sum()
}

fun Int.primeDividers(): Set<Int> {
    return this.properDivisors().filter{it.isPrime()}.toSet()
}
