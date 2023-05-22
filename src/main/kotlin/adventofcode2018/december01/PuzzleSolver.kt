package adventofcode2018.december01

import adventofcode2018.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines.map{it.toInt()}.sum()
    }
}


