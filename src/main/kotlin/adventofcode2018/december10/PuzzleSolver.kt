package adventofcode2018.december10

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinatesystem.Coordinate
import tool.coordinatesystem.area
import tool.coordinatesystem.printAsGrid

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    //position=< 9,  1> velocity=< 0,  2>
    private val coordList = inputLines.map{ Coordinate.of(it.substringAfter("position=<").substringBefore(">")) }
    private val velocityList = inputLines.map{ Coordinate.of(it.substringAfter("velocity=<").substringBefore(">")) }

    override fun resultPartOne(): Any {
        var iterationCount = 0
        var last: List<Coordinate>
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
}


