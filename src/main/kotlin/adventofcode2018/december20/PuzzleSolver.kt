package adventofcode2018.december20

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Pos

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val startPos = Pos(3,3)
    private val grid = inputLines.first().makeGrid(startPos)

    override fun resultPartOne(): Any {
        return grid.shortestPathToAll(startPos).values.max()
//        return grid.keys.map{ to -> grid.shortestPath(startPos, to) }.max()
    }

    override fun resultPartTwo(): Any {
        return grid.shortestPathToAll(startPos).values.count { it >= 1000 }
//        return grid.keys.map{ to -> grid.shortestPath(startPos, to) }.count { it >= 1000 }
    }

    private fun String.makeGrid(startPos: Pos = Pos(0,0)): Map<Pos, Set<Pos>> {
        val result = mutableMapOf<Pos, MutableSet<Pos>>()
        var currentPos = startPos
        val stack = ArrayDeque<Pos>()
        this.forEach { ch ->
            when(ch) {
                '^', '('  -> stack.addLast(currentPos)
                '$', ')' -> currentPos = stack.removeLast()
                '|' -> currentPos = stack.last()
                else -> {
                    val next = currentPos.neighbor(ch)
                    result.connectRooms(currentPos, next)
                    currentPos = next
                }
            }
        }
        return result
    }

    private fun MutableMap<Pos, MutableSet<Pos>>.connectRooms(from: Pos, to: Pos) {
        this.getOrPut(from) { mutableSetOf() }.add(to)
        this.getOrPut(to) { mutableSetOf() }.add(from)
    }

    private fun Pos.neighbor(ch: Char): Pos {
        return when(ch) {
            'N' -> this.north()
            'S' -> this.south()
            'W' -> this.west()
            'E' -> this.east()
            else -> throw Exception("Surprising character")
        }
    }

//    private fun Map<Pos, Set<Pos>>.shortestPath(from: Pos, to: Pos): Int {
//        val queue = ArrayDeque<Pair<Pos, Int>>()
//        val visited = mutableSetOf<Pos>()
//        queue.addFirst(Pair(from,0))
//        while (queue.isNotEmpty()) {
//            val (current, stepsDone) = queue.removeLast()
//            if (current == to)
//                return stepsDone
//
//            this[current]!!.filterNot { it in visited }.forEach { queue.addLast(Pair(it, stepsDone+1)) }
//            visited.add(current)
//        }
//        return -1
//    }

    private fun Map<Pos, Set<Pos>>.shortestPathToAll(from: Pos): Map<Pos, Int> {
        val result = mutableMapOf<Pos, Int>()
        val queue = ArrayDeque<Pair<Pos, Int>>()
        val visited = mutableSetOf<Pos>()
        queue.addFirst(Pair(from,0))
        while (queue.isNotEmpty()) {
            val (current, stepsDone) = queue.removeLast()
            result[current] = stepsDone
            this[current]!!.filterNot { it in visited }.forEach { queue.addLast(Pair(it, stepsDone+1)) }
            visited.add(current)
        }
        return result
    }


}


