package adventofcode2018.december23

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinate.threedimensional.Point3D

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    //pos=<1,3,1>, r=1
    val nanoBotList =
        inputLines.map {
            NanoBot(Point3D.of(it.substringAfter("pos=").substringBefore(", r=")), it.substringAfter("r=").toInt())
        }


    override fun resultPartOne(): Any {
        val strongRobot = nanoBotList.maxBy { it.range }
        return nanoBotList.count { it.location.manhattanDistance(strongRobot.location) <= strongRobot.range }
    }

    //see https://todd.ginsberg.com/post/advent-of-code/2018/day23/ for solution
    // not solved by myself
    override fun resultPartTwo(): Any {
        // make a map per NanoBot with a set of other bots that are in each other's reach
        val neighbors: Map<NanoBot, Set<NanoBot>> = nanoBotList.map { bot ->
            Pair(bot, nanoBotList.filterNot { it == bot }.filter { bot.withinRangeOfSharedPoint(it) }.toSet())
        }.toMap()

        //Zoek de largest clique...
        val clique: Set<NanoBot> = BronKerbosch(neighbors).largestClique()

        //En in deze kliek, ga op zoek naar de ... geen idee
        return clique.map { it.location.manhattanDistance(Point3D.origin) - it.range }.max()!!
    }

}

//----------------------------------------------------------------------------------------------------------------------

data class NanoBot(val location: Point3D, val range: Int) {
    fun withinRangeOfSharedPoint(other: NanoBot): Boolean =
        location.manhattanDistance(other.location) <= (range + other.range)
}

class BronKerbosch<T>(private val neighbors: Map<T, Set<T>>) {

    private var bestR: Set<T> = emptySet()

    fun largestClique(): Set<T> {
        execute(neighbors.keys)
        return bestR
    }

    private fun execute(
        p: Set<T>,
        r: Set<T> = emptySet(),
        x: Set<T> = emptySet()
    ) {
        if (p.isEmpty() && x.isEmpty()) {
            // We have found a potential best R value, compare it to the best so far.
            if (r.size > bestR.size) bestR = r
        } else {
            val mostNeighborsOfPandX: T = (p + x).maxBy { neighbors.getValue(it).size }!!
            val pWithoutNeighbors = p.minus(neighbors[mostNeighborsOfPandX]!!)
            pWithoutNeighbors.forEach { v ->
                val neighborsOfV = neighbors[v]!!
                execute(
                    p.intersect(neighborsOfV),
                    r + v,
                    x.intersect(neighborsOfV)
                )
            }
        }
    }
}