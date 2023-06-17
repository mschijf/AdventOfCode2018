package adventofcode2018.december10

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinatesystem.Pos
import tool.coordinatesystem.printAsGrid

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    //position=< 9,  1> velocity=< 0,  2>
    private val coordList = inputLines.map{ Pos.of(it.substringAfter("position=<").substringBefore(">")) }
    private val velocityList = inputLines.map{ Pos.of(it.substringAfter("velocity=<").substringBefore(">")) }

    override fun resultPartOne(): Any {
        var iterationCount = 0
        var last: List<Pos>
        var next = coordList
        do {
            last = next
            next = last.mapIndexed { index, coordinate -> coordinate.plusXY(velocityList[index].x, velocityList[index].y ) }
            ++iterationCount
        } while (next.area() < last.area())
        last.toSet().printAsGrid("..", "##")
        println("Seconds waited (part2): ${iterationCount-1}")
        return ""
    }

    override fun resultPartTwo(): Any {
        return "^^^^^^^"
    }

    private fun List<Pos>.area(): Long {
        if (this.isEmpty())
            return 0
        val minX = this.minOf { it.x }
        val minY = this.minOf { it.y }
        val maxX = this.maxOf { it.x }
        val maxY = this.maxOf { it.y }
        return (maxX - minX + 1L) * (maxY - minY + 1L)
    }

}


