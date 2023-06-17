package adventofcode2018.december17

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinatesystem.Coordinate
import tool.coordinatesystem.printGrid

fun main() {
    PuzzleSolver(test=false).showResult()
}

//33366 ==> too high
class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val waterSpring = Coordinate(500,0)
    private val claySet = inputLines.flatMap{ line -> line.coordinateLine()}.toSet()
    private val minX = claySet.minOf { it.x }
    private val minY = claySet.minOf { it.y }
    private val maxX = claySet.maxOf { it.x }
    private val maxY = claySet.maxOf { it.y }
    private val upperLeft = Coordinate(minX-1, 0)
    private val lowerRight = Coordinate(maxX+1, maxY+1)

    private val wetSandSet = mutableSetOf<Coordinate>()
    private val waterSet = mutableSetOf<Coordinate>()

    override fun resultPartOne(): Any {
        dropDown(waterSpring)
        //printWaterWell()
        return (waterSet + wetSandSet).filter { it.y in minY..maxY }.size
    }

    override fun resultPartTwo(): Any {
        return waterSet.size
    }

    private fun printWaterWell() {
        Pair(upperLeft, lowerRight).printGrid { c ->
            when (c) {
                in setOf(waterSpring)-> "+"
                in claySet -> "#"
                in waterSet -> "~"
                in wetSandSet -> "|"
                else -> "."
            }
        }
    }

    private fun String.coordinateLine(): List<Coordinate> {
        return when {
            this.startsWith("x") -> {
                val x = this.substringAfter("x=").substringBefore(",").toInt()
                val yRange = this.substringAfter("y=").substringBefore("..").toInt()..this.substringAfter("..").toInt()
                yRange.map { y -> Coordinate(x, y) }
            }

            this.startsWith("y") ->  {
                val y = this.substringAfter("y=").substringBefore(",").toInt()
                val xRange = this.substringAfter("x=").substringBefore("..").toInt()..this.substringAfter("..").toInt()
                xRange.map { x -> Coordinate(x, y) }
            }

            else -> throw Exception("unexpected start of string")
        }
    }

    private fun dropDown(c: Coordinate) {
        if (c in claySet || c.y > maxY)
            return
        if (c in wetSandSet)
            return
        if (c in waterSet)
            return

        wetSandSet += c

        dropDown(c.plusY(1))
        if (c.plusY(1) in claySet || c.plusY(1) in waterSet) {
            dropDown(c.left())
            dropDown(c.right())
        }

        if (c.canBeFilled()) {
            c.fillWithWater()
        }
    }
    private fun Coordinate.canBeFilled(): Boolean {
        var walker = this.left()
        while (walker !in claySet && (walker.plusY(1) in claySet || walker.plusY(1) in waterSet)) {
            walker = walker.left()
        }
        if (walker !in claySet)
            return false

        walker = this.right()
        while (walker !in claySet && (walker.plusY(1) in claySet || walker.plusY(1) in waterSet)) {
            walker = walker.right()
        }
        if (walker !in claySet)
            return false
        return true
    }

    private fun Coordinate.fillWithWater() {
        waterSet += this
        var walker = this.left()
        while (walker !in claySet) {
            waterSet += walker
            walker = walker.left()

        }
        walker = this.right()
        while (walker !in claySet) {
            waterSet += walker
            walker = walker.right()
        }
    }

}


