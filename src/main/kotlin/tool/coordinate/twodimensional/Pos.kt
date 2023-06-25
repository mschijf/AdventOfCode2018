package tool.coordinate.twodimensional

import kotlin.math.absoluteValue

data class Pos(val x: Int, val y: Int) {
    fun plusXY(dx: Int, dy: Int) = Pos(x+dx, y+dy)

    fun plusX(dx: Int) = plusXY(dx, 0)
    fun plusY(dy: Int) = plusXY(0, dy)

    fun moveOneStep(dir: Direction) = plusXY(dir.dX(), dir.dY())
    fun moveOneStep(dir: WindDirection) = plusXY(dir.dX(), dir.dY())

    fun neighbors() = listOf(up(), down(), left(), right())
    fun allWindDirectionNeighbors() = listOf(north(), northeast(), east(), southeast(), south(), southwest(), west(), northwest())

    fun up() = moveOneStep(Direction.UP)
    fun down() = moveOneStep(Direction.DOWN)
    fun left() = moveOneStep(Direction.LEFT)
    fun right() = moveOneStep(Direction.RIGHT)

    fun north() = moveOneStep(WindDirection.NORTH)
    fun south() = moveOneStep(WindDirection.SOUTH)
    fun east() = moveOneStep(WindDirection.EAST)
    fun west() = moveOneStep(WindDirection.WEST)
    fun northeast() = moveOneStep(WindDirection.NORTHEAST)
    fun southeast() = moveOneStep(WindDirection.SOUTHEAST)
    fun northwest() = moveOneStep(WindDirection.NORTHWEST)
    fun southwest() = moveOneStep(WindDirection.SOUTHWEST)

    fun manhattanDistance(otherPos: Pos) = (otherPos.x - x).absoluteValue + (otherPos.y - y).absoluteValue

    companion object {
        fun of(input: String): Pos = input
            .removeSurrounding("<", ">")
            .removeSurrounding("(", ")")
            .removeSurrounding("[", "]")
            .removeSurrounding("{", "}")
            .split(",").run { Pos(this[0].trim().toInt(), this[1].trim().toInt()) }
    }

    private fun Direction.dX() =
        when(this) {
            Direction.DOWN -> 0
            Direction.UP -> 0
            Direction.LEFT -> -1
            Direction.RIGHT -> 1
        }
    private fun Direction.dY() =
        when(this) {
            Direction.DOWN -> 1
            Direction.UP -> -1
            Direction.LEFT -> 0
            Direction.RIGHT -> 0
        }

    private fun WindDirection.dX(): Int =
        when(this) {
            WindDirection.NORTH -> 0
            WindDirection.SOUTH -> 0
            WindDirection.EAST -> 1
            WindDirection.WEST -> -1
            WindDirection.NORTHEAST -> WindDirection.EAST.dX()
            WindDirection.NORTHWEST -> WindDirection.WEST.dX()
            WindDirection.SOUTHEAST -> WindDirection.EAST.dX()
            WindDirection.SOUTHWEST -> WindDirection.WEST.dX()
        }
    private fun WindDirection.dY(): Int =
        when (this) {
            WindDirection.NORTH -> -1
            WindDirection.SOUTH -> 1
            WindDirection.EAST -> 0
            WindDirection.WEST -> 0
            WindDirection.NORTHEAST -> WindDirection.NORTH.dY()
            WindDirection.NORTHWEST -> WindDirection.NORTH.dY()
            WindDirection.SOUTHEAST -> WindDirection.SOUTH.dY()
            WindDirection.SOUTHWEST -> WindDirection.SOUTH.dY()
        }

}

