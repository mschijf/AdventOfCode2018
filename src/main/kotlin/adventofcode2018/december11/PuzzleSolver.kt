package adventofcode2018.december11

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point2D

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val gridSerialNumber = if (test) 42 else 7139

    private val powerLevelMap = createPowerLevelMap(gridSerialNumber)

    override fun resultPartOne(): Any {
//        println(Coordinate(3,5).powerLevel(gridSerialNumber))
//        println(Coordinate(122,79).powerLevel(57))
//        println(Coordinate(217,196).powerLevel(39))
//        println(Coordinate(101, 153).powerLevel(71))

        return (1..298).flatMap {x ->
            (1..298).map {y ->
                Point2D(x,y)
            }
        }.maxBy {coordinate ->  coordinate.totalPower(3, gridSerialNumber)}
    }

    override fun resultPartTwo(): Any {
//        return (2..2)
//            .map { size -> calculateMaxPower(size, gridSerialNumber) }
//            .maxBy { it.second }


        val start = powerLevelMap.toMutableMap()
        var max = start.filter { it.key.x <= 300 && it.key.y <= 300 }.maxBy { it.value }
        var best = Triple(max.key, max.value, 1)
        for (size in 2..300) {
            print(size)
            start.extend(size)
            val localMax = start.filter { it.key.x <= 301-size && it.key.y <= 301-size }.maxBy { it.value }
            if (localMax.value > max.value) {
                max = localMax
                best = Triple(localMax.key, localMax.value, size)
                print(" Found better one!")
            }
            println()
        }
        return best
    }


    private fun calculateMaxPower(size: Int, serialNumber: Int) : Triple<Point2D, Int, Int> {
        val bestPoint2D =  (1..(301-size)).flatMap { x ->
            (1..(301-size)).map {y ->
                Point2D(x,y)
            }
        }.maxBy {coordinate ->  coordinate.totalPower(size, serialNumber)}

        return Triple(bestPoint2D, bestPoint2D.totalPower(size, serialNumber), size)
    }

    private fun Point2D.totalPower(size: Int, serialNumber: Int): Int {
         val serie = (0 until size).flatMap { dx ->
             (0 until size). map { dy ->
                 this.plusXY(dx, dy)
             }
         }
        return serie.sumOf { powerLevelMap[it]!! }
    }

    private fun Point2D.powerLevel(serialNumber: Int): Int {
        val rackID = this.x + 10
        return  ((rackID * this.y + serialNumber) * rackID / 100) % 10 - 5
    }

    private fun createPowerLevelMap(serialNumber: Int): Map<Point2D, Int> {
        return (1..300).flatMap {x ->
            (1..300).map {y ->
                Point2D(x,y)
            }
        }.associateWith {coordinate ->  coordinate.powerLevel(serialNumber)}
    }

    private fun MutableMap<Point2D, Int>.extend(size: Int) {
        for (x in 1..301-size) {
            for (y in 1..301-size) {
                val sumX = (x..x+size-1).sumOf { dx -> powerLevelMap[Point2D(dx, y+size-1)]!! }
                val sumY = (y..y+size-2).sumOf { dy -> powerLevelMap[Point2D(x+size-1, dy)]!! }
                this[Point2D(x,y)] = this[Point2D(x,y)]!! + sumX + sumY
            }
        }
    }

}


