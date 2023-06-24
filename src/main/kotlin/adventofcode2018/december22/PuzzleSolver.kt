package adventofcode2018.december22

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinatesystem.Pos
import tool.coordinatesystem.printAsGrid
import java.util.*

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val depth = inputLines.first().substringAfter("depth: ").toInt()
    private val target = Pos.of(inputLines.last().substringAfter("target: "))
    private val mouth = Pos(0,0)

    private val geologicalIndex = createGeologicalIndexMap()

    override fun resultPartOne(): Any {
        //geologicalIndex.print()
        return geologicalIndex.riskLevel()
    }

    override fun resultPartTwo(): Any {
        val x = solve()
        //geologicalIndex.print()
        return x
    }


    //1086 --> too high
    //1078 --> too high
    //1054 --> Good!!

    private fun solve(): Int {
        val compareByMinutes: Comparator<Triple<Pos, Gear, Int>> = compareBy { it.third + it.first.manhattanDistance(target) }
        var nodesVisited = 0L
        val visited = mutableMapOf<Pair<Pos, Gear>, Int>()
        var bestValue = Int.MAX_VALUE
        val priorityQueue = PriorityQueue(compareByMinutes)
        priorityQueue.add(Triple(mouth, Gear.TORCH, 0))
        while (priorityQueue.isNotEmpty()) {
            nodesVisited++
            val (current, gear, minutesPassed) = priorityQueue.remove()
            val vistedKey = Pair(current, gear)
            if (minutesPassed >= visited.getOrDefault(vistedKey, Int.MAX_VALUE))
                continue
            if (minutesPassed + current.manhattanDistance(target) >= bestValue)
                continue
            if (current == target) {
                val targetValue = minutesPassed + if (gear == Gear.TORCH) 0 else 7
                if (targetValue < bestValue) {
                    bestValue = targetValue
                    visited[vistedKey] = bestValue
                }
            } else {
                visited[vistedKey] = minutesPassed
            }
            current.neighbors()
                .filter { it.x >= 0 && it.y >= 0 }
                .forEach { neighbor ->
                    geologicalIndex.addRowOrCol(neighbor)
                    val type = geologicalIndex[neighbor]!!.type()
                    if (gear.isLegalFor(type)) {
                        priorityQueue.add(Triple(neighbor, gear, minutesPassed + 1))
                    } else {
                        val newGear = usableGear(geologicalIndex[current]!!.type(), geologicalIndex[neighbor]!!.type())
                        priorityQueue.add(Triple(neighbor, newGear, minutesPassed + 7 + 1))
                    }
                }
        }
        println("Nodes visited: $nodesVisited")
        return bestValue
    }


    private fun Gear.isLegalFor(type: Int): Boolean {
        return when(type) {
            0 -> this == Gear.TORCH || this == Gear.CLIMBING
            1 -> this == Gear.CLIMBING || this == Gear.NEITHER
            2 -> this == Gear.TORCH || this == Gear.NEITHER
            else -> throw Exception("huuuhuhh?")

        }
    }
    private fun usableGear(type1: Int, type2: Int): Gear {
        when (type1) {
            0 -> return if (type2 == 1) Gear.CLIMBING else Gear.TORCH
            1 -> return if (type2 == 0) Gear.CLIMBING else Gear.NEITHER
            2 -> return if (type2 == 0) Gear.TORCH else Gear.NEITHER
        }
        throw Exception("impossible combi")
    }

    private fun MutableMap<Pos, Int>.addRowOrCol(pos: Pos) {
        if (this.containsKey(pos))
            return
        if (this.containsKey(pos.up())) {
            val maxX = geologicalIndex.keys.maxOf { it.x }
            geologicalIndex[Pos(0, pos.y)] = pos.y * 48271
            (1..maxX).forEach { x ->
                val current = Pos(x, pos.y)
                this[current] = this[current.left()]!!.erosionLevel() * this[current.up()]!!.erosionLevel()
            }
        } else {
            val maxY = geologicalIndex.keys.maxOf { it.y }
            geologicalIndex[Pos(pos.x, 0)] = pos.x * 16807
            (1..maxY).forEach { y ->
                val current = Pos(pos.x, y)
                this[current] = this[current.left()]!!.erosionLevel() * this[current.up()]!!.erosionLevel()
            }

        }
    }

    private fun createGeologicalIndexMap(): MutableMap<Pos, Int> {
        val geologicalIndex = mutableMapOf<Pos, Int>()
        geologicalIndex[mouth] = 0
        (1 .. target.x).forEach{x -> geologicalIndex[Pos(x,0)] = x * 16807}
        (1 .. target.y).forEach { y -> geologicalIndex[Pos(0, y)] = y * 48271}

        (1 .. target.y).forEach {y ->
            (1 .. target.x).forEach {x ->
                val current = Pos(x,y)
                geologicalIndex[current] = geologicalIndex[current.left()]!!.erosionLevel() * geologicalIndex[current.up()]!!.erosionLevel()
            }
        }
        geologicalIndex[target] = 0
        return geologicalIndex
    }

    private fun Map<Pos, Int>.riskLevel(): Int {
        return this.filter{it.key.x <= target.x && it.key.y <= target.y}.values.sumOf { it.type() }
    }

    private fun Map<Pos, Int>.print() {
        this.printAsGrid("?") { it.typeChar().toString() }
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

enum class Gear {
    TORCH, CLIMBING, NEITHER;
}