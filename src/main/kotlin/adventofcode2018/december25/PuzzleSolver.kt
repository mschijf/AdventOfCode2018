package adventofcode2018.december25

import adventofcode2018.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val pointList = inputLines.map{Point4D.of(it)}

    override fun resultPartOne(): Any {
        var constellations = emptyList<Set<Point4D>>()

        pointList.forEach { point ->
            var extended = constellations.filter{it.inConstellation(point)}.flatMap{it+point}.toSet()
            val notExtended = constellations.filterNot { it.inConstellation(point) }
            if (extended.isEmpty()) {
                extended = setOf(point)
            }
            constellations = notExtended + listOf(extended)
        }
        println(constellations.map{it.size})
        return constellations.size
    }

    private fun Set<Point4D>.inConstellation(aPoint:Point4D): Boolean {
        return this.any{it.manhattanDistance(aPoint) <= 3}
    }
}


data class Point4D(val x: Int, val y: Int, val z: Int, val t: Int) {
    fun manhattanDistance(otherPos: Point4D) = (otherPos.x - x).absoluteValue + (otherPos.y - y).absoluteValue + (otherPos.z - z).absoluteValue + (otherPos.t - t).absoluteValue

    companion object {
        fun of(input: String): Point4D {
            val (x,y,z,t) = input.split(",").map{it.trim().toInt()}
            return Point4D(x,y,z,t)
        }
    }
}