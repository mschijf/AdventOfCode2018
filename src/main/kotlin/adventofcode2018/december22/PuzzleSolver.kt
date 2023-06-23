package adventofcode2018.december22

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinatesystem.Pos

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val depth = inputLines.first().substringAfter("depth: ").toInt()
    private val target = Pos.of(inputLines.last().substringAfter("target: "))
    private val mouth = Pos(0,0)

    override fun resultPartOne(): Any {
        val geologicalIndex = createGeologicalIndexGrid()
        geologicalIndex.print()
        return geologicalIndex.riskLevel()
    }

    private fun createGeologicalIndexGrid(): Array<IntArray> {
        val geologicalIndex = Array(target.y+1){y -> IntArray(target.x+1){0}  }
        geologicalIndex[0][0] = 0
        for (x in 1 .. target.x)
            geologicalIndex[0][x] = x * 16807
        for (y in 1 .. target.y)
            geologicalIndex[y][0] = y * 48271

        for (y in 1 .. target.y) {
            for (x in 1 .. target.x) {
                geologicalIndex[y][x] = geologicalIndex[y-1][x].erosionLevel() * geologicalIndex[y][x-1].erosionLevel()
            }
        }
        geologicalIndex[target.y][target.x] = 0
        return geologicalIndex
    }

    private fun Array<IntArray>.print() {
        for (y in 0 until this.size) {
            for (x in 0 until this[y].size) {
                print(this[y][x].typeChar())
            }
            println()
        }
    }

    private fun Array<IntArray>.riskLevel(): Int {
        var sum = 0
        for (y in 0 until this.size) {
            for (x in 0 until this[y].size) {
                sum += this[y][x].type()
            }
        }
        return sum
    }


    private fun Int.erosionLevel() = (this + depth) % 20183
    private fun Int.type() = this.erosionLevel() % 3
    private fun Int.typeChar() = when(this.erosionLevel() % 3) {
        0 -> '.' //rocky
        1 -> '=' //wet
        2 -> '|' //narrow
        else -> '?'
    }
}


