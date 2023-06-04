package adventofcode2018.december09

import adventofcode2018.PuzzleSolverAbstract
import tool.collectionspecials.emptyCircularList

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val numberOfPLayers = inputLines.first().split(" ").first().toInt()
    private val highestMarble = inputLines.first().split(" ").run{this[6]}.toLong()

    override fun resultPartOne(): Any {
        return solveForHighestMarble(highestMarble)
    }

    override fun resultPartTwo(): Any {
        return solveForHighestMarble(highestMarble*100)
    }

    private fun solveForHighestMarble(highestMarble: Long): Long {
        val playerScores = Array(numberOfPLayers){0L}
        val circle = emptyCircularList<Long>()
        var currentNode = circle.add(0)

        var currentPlayer = 0
        for (i in 1..highestMarble) {
            if (i % 23 == 0L) {
                currentNode -= 6
                val valueRemovedNode = circle[currentNode-1]
                circle.removeAt(currentNode-1)
                playerScores[currentPlayer] += (i + valueRemovedNode)
            } else {
                currentNode = circle.add(currentNode+2, i)
            }
            currentPlayer = (currentPlayer + 1) % numberOfPLayers
        }

        return playerScores.max()
    }
}


