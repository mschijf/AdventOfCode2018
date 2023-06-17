package adventofcode2018.december06

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinatesystem.Pos
import tool.coordinatesystem.printAsGrid

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val coordList = inputLines.map {Pos.of(it)}

    private val maxX = coordList.maxOf{ it.x }
    private val maxY = coordList.maxOf{ it.y }

    override fun resultPartOne(): Any {
        val gridMap = createMapPuzzleOne()
        gridMap.printAsGrid { it.gridValueToString() }
        val singleFields = gridMap.filter { it.value.size == 1 }.map{it.key to it.value.first()}.toMap()
        val borderFields = singleFields.filter{it.key.isBorder()}.values.toSet()
        val resultFields = singleFields.values.filterNot{it in borderFields}.groupingBy { it }.eachCount()
        return resultFields.values.max()
    }

    override fun resultPartTwo(): Any {
        val maxManhattanDistance = if (test) 32 else 10_000
        val gridMap = createMapPuzzleTwo()
        gridMap.printAsGrid { item -> if (item < 32) "#" else "." }
        return gridMap.values.count { it < maxManhattanDistance }
    }

    private fun List<Pos>.gridValueToString(): String {
        return if (this.size > 1) "." else { ('a'+ coordList.indexOf( this.first() )).toString() }
    }

    private fun Pos.isBorder() : Boolean {
        return this.x == 0 || this.y == 0 || this.x == maxX || this.y == maxY
    }


    private fun createMapPuzzleOne(): Map<Pos, List<Pos>> {
        return (0..maxX+1).flatMap { x -> (0..maxY).map { y -> Pos(x, y) } }
            .associateWith { coord -> coordList.groupBy { it.manhattanDistance(coord) }.minBy { it.key }.value }
    }

    private fun createMapPuzzleTwo(): Map<Pos, Int> {
        return (0..maxX).flatMap { x -> (0..maxY).map { y -> Pos(x, y) } }
            .associateWith { coord -> coordList.sumOf { it.manhattanDistance(coord) } }
    }

//    private fun Map<GridPos, List<GridPos>>.print() {
//        val maxX = this.keys.maxOf{ it.x }
//        val maxY = this.keys.maxOf{ it.y }
//        for (y in 0..maxY) {
//            for (x in 0..maxX) {
//                if (this[GridPos(x,y)]!!.size > 1) {
//                    print (".")
//                } else {
//                    val index = coordList.indexOf( this[GridPos(x,y)]!!.first() )
//                    print('a'+index)
//                }
//            }
//            println()
//        }
//    }

    private fun Map<Pos, Int>.print2() {
        val maxX = this.keys.maxOf{ it.x }
        val maxY = this.keys.maxOf{ it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (this[Pos(x,y)]!! < 32) {
                    print ("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }
}


