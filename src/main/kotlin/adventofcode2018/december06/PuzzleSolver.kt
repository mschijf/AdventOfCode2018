package adventofcode2018.december06

import adventofcode2018.PuzzleSolverAbstract
import tool.position.Coordinate

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val coordList = inputLines.map {Coordinate.of(it)}

    private val maxX = coordList.maxOf{ it.x }
    private val maxY = coordList.maxOf{ it.y }


    override fun resultPartOne(): Any {
        val gridMap = createMapPuzzleOne()
//        gridMap.print()
        val singleFields = gridMap.filter { it.value.size == 1 }.map{it.key to it.value.first()}.toMap()
        val borderFields = singleFields.filter{it.key.isBorder()}.values.toSet()
        val resultFields = singleFields.values.filter{it !in borderFields}.groupingBy { it }.eachCount()
        return resultFields.values.max()
    }

    override fun resultPartTwo(): Any {
        val maxManhattanDistance = if (test) 32 else 10_000
        val gridMap = createMapPuzzleTwo()
//        gridMap.print2()
        return gridMap.values.count { it < maxManhattanDistance }
    }

    private fun Coordinate.isBorder() : Boolean {
        return this.x == 0 || this.y == 0 || this.x == maxX || this.y == maxY
    }


    private fun createMapPuzzleOne(): Map<Coordinate, List<Coordinate>> {
        return (0..maxX).flatMap { x -> (0..maxY).map { y -> Coordinate(x, y) } }
            .associateWith { coord -> coordList.groupBy { it.manhattanDistance(coord) }.minBy { it.key }.value }
    }

    private fun createMapPuzzleTwo(): Map<Coordinate, Int> {
        return (0..maxX).flatMap { x -> (0..maxY).map { y -> Coordinate(x, y) } }
            .associateWith { coord -> coordList.sumOf { it.manhattanDistance(coord) } }
    }

    private fun Map<Coordinate, List<Coordinate>>.print() {
        val maxX = this.keys.maxOf{ it.x }
        val maxY = this.keys.maxOf{ it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (this[Coordinate(x,y)]!!.size > 1) {
                    print (".")
                } else {
                    val index = coordList.indexOf( this[Coordinate(x,y)]!!.first() )
                    print('a'+index)
                }
            }
            println()
        }
    }

    private fun Map<Coordinate, Int>.print2() {
        val maxX = this.keys.maxOf{ it.x }
        val maxY = this.keys.maxOf{ it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (this[Coordinate(x,y)]!! < 32) {
                    print ("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }
}


