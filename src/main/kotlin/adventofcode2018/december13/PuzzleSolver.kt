package adventofcode2018.december13

import adventofcode2018.PuzzleSolverAbstract
import tool.coordinatesystem.Coordinate
import tool.coordinatesystem.Direction

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val rails = readRails()

    override fun resultPartOne(): Any {
        val carts = readCarts()

        while (true) {
            carts.sorted().forEach {cart ->
                cart.step()
                val crashedCars = carts.filterNot{it === cart}.filter { it.pos() == cart.pos() }
                if (crashedCars.isNotEmpty()) {
                    return cart.pos()
                }
            }
        }
    }

    override fun resultPartTwo(): Any {
        val carts = readCarts()

        while (carts.count { !it.hasCrashed } > 1) {
            carts.sorted().forEach {cart ->
                if (!cart.hasCrashed) {
                    cart.step()
                    val crashedCars = carts.filterNot { it === cart}.filterNot{it.hasCrashed}.filter { it.pos() == cart.pos() }
                    if (crashedCars.isNotEmpty()) {
                        cart.hasCrashed = true
                        crashedCars.forEach { crt -> crt.hasCrashed = true }
                    }
                }
            }
        }
        return carts.filter { !it.hasCrashed }
    }

    private fun readRails(): Map<Coordinate, Char> =
        inputLines.flatMapIndexed { y: Int, s: String ->
            s.mapIndexed { x, c ->
                when (c) {
                    in "<>" -> Coordinate(x,y) to '-'
                    in "^v" -> Coordinate(x,y) to '|'
                    else -> Coordinate(x,y) to c
                }
            }
        }.filter{it.second != ' '}.toMap()

    private fun readCarts() =
        inputLines.flatMapIndexed { y: Int, s: String ->
            s.mapIndexed { x, c ->
                if (c in "<>^v") Cart(rails, Coordinate(x,y), c.toDirection()) else null
            }
        }.filterNotNull()

    private fun Char.toDirection() =
        Direction.values().first { it.directionChar == this }
}

class Cart (
    private val rails: Map<Coordinate, Char>,
    private var pos: Coordinate,
    private var direction: Direction): Comparable<Cart> {
    private var turnTime = 0
    var hasCrashed = false

    fun step() {
        pos = pos.plusXY(direction.dCol(), direction.dRow())
//        pos = pos.moveOneStep(direction) ==> cannot use this one, since Direction.Down does y-1 where the y-coordinate increases when going down
        direction = when (rails[pos]!!) {
            '-', '|' -> direction
            '/' -> when (direction) {
                Direction.UP, Direction.DOWN -> direction.rotateRight()
                Direction.LEFT, Direction.RIGHT -> direction.rotateLeft()
            }
            '\\' -> when (direction) {
                Direction.UP, Direction.DOWN -> direction.rotateLeft()
                Direction.LEFT, Direction.RIGHT -> direction.rotateRight()
            }
            '+' -> {
                when ( (turnTime++) % 3 ) {
                    0 -> direction.rotateLeft()
                    1 -> direction
                    2 -> direction.rotateRight()
                    else -> throw Exception("Not 0,1,2 in turnTime")
                }
            }
            else -> throw Exception("Unexpected Char")
        }
    }

    override fun compareTo(other: Cart): Int {
        return compareValuesBy(this, other, { it.pos.y }, { it.pos.x })
    }

    override fun toString(): String {
        return "(${pos.x}, ${pos.y}): $direction"
    }

    fun pos(): Coordinate = pos


}
