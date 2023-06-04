package adventofcode2018.december09

import adventofcode2018.PuzzleSolverAbstract
import tool.collectionspecials.CircularLinkedList

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
        val circle = CircularLinkedList<Long>()
        var currentNode = circle.init(0)

        var currentPlayer = 0
        for (i in 1..highestMarble) {
            if (i % 23 == 0L) {
                val nodeToBeRemoved = circle.getPreviousNode(currentNode, 7)
                currentNode = nodeToBeRemoved.next
                playerScores[currentPlayer] += (i + nodeToBeRemoved.data)
                circle.remove(nodeToBeRemoved)
            } else {
                currentNode = circle.insertAfter(currentNode.next, i)
            }
            currentPlayer = (currentPlayer + 1) % numberOfPLayers
        }

        return playerScores.max()
    }
}


