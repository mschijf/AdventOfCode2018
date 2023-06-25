package adventofcode2018.december17

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Pos
import tool.coordinate.twodimensional.printGrid

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val waterSpring = Pos(500,0)
    private val claySet = inputLines.flatMap{ line -> line.toPosList()}.toSet()
    private val minX = claySet.minOf { it.x }
    private val minY = claySet.minOf { it.y }
    private val maxX = claySet.maxOf { it.x }
    private val maxY = claySet.maxOf { it.y }
    private val upperLeft = Pos(minX-1, 0)
    private val lowerRight = Pos(maxX+1, maxY+1)

    private val wetSandSet = mutableSetOf<Pos>()
    private val waterSet = mutableSetOf<Pos>()

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

    private fun String.toPosList(): List<Pos> {
        return when {
            this.startsWith("x") -> {
                val x = this.substringAfter("x=").substringBefore(",").toInt()
                val yRange = this.substringAfter("y=").substringBefore("..").toInt()..this.substringAfter("..").toInt()
                yRange.map { y -> Pos(x, y) }
            }

            this.startsWith("y") ->  {
                val y = this.substringAfter("y=").substringBefore(",").toInt()
                val xRange = this.substringAfter("x=").substringBefore("..").toInt()..this.substringAfter("..").toInt()
                xRange.map { x -> Pos(x, y) }
            }

            else -> throw Exception("unexpected start of string")
        }
    }

    private fun dropDown(c: Pos) {
        if (c in claySet || c.y > maxY)
            return
        if (c in wetSandSet)
            return
        if (c in waterSet)
            return

        wetSandSet += c

        dropDown(c.down())
        if (c.down() in claySet || c.down() in waterSet) {
            dropDown(c.left())
            dropDown(c.right())
        }

        if (c.canBeFilled()) {
            c.fillWithWater()
        }
    }
    private fun Pos.canBeFilled(): Boolean {
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

    private fun Pos.fillWithWater() {
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


