package adventofcode2018.december03

import adventofcode2018.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val rectangleList = inputLines.map{Rectangle.fromString(it)}

    override fun resultPartOne(): Any {
        return rectangleList
            .mapCombinedAll { rectangle, rectangle2 -> rectangle.intersectionOrNull(rectangle2) }
            .fold(RectangleSet()) {acc, next -> acc.plus(next)}
            .area()

// Todd Ginsberg Solution
//
//        return rectangleList
//            .flatMap { it.asCoordinates() }  // List<Coordinate>
//            .groupingBy { it }
//            .eachCount()                     // Map<Coordinate, Int>
//            .count { it.value > 1 }
    }

    public inline fun <S, T> List<T>.mapCombinedAll(combineOperation: (T, T) -> S): List<S> {
        var result = mutableListOf<S>()
        for (i in 0 until this.size-1) {
            for (j in i+1 until this.size) {
                result += combineOperation(this[i], this[j])
            }
        }
        return result
    }

    override fun resultPartTwo(): Any {
        var firstNonOverlapped = 0
        for (i in 0 until rectangleList.size) {
            var hasOverlap = false
            for (j in 0 until rectangleList.size) {
                if (i != j && rectangleList[i].intersectionOrNull(rectangleList[j]) != null) {
                    hasOverlap = true
                    break
                }
            }
            if (!hasOverlap) {
                firstNonOverlapped = i+1
                break
            }
        }
        return firstNonOverlapped
    }
}


