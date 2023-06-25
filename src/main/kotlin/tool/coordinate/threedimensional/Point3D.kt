package tool.coordinate.threedimensional

import kotlin.math.absoluteValue

data class Point3D(val x: Int, val y: Int, val z: Int) {

    fun plusXYZ(dx: Int, dy: Int, dz: Int) = Point3D(x+dx, y+dy, z+dz)
    fun plusX(dx: Int) = plusXYZ(x+dx, y, z)
    fun plusY(dy: Int) = plusXYZ(x, y+dy, z)
    fun plusZ(dz: Int) = plusXYZ(x, y, z+dz)

    fun manhattanDistance(otherPos: Point3D) = (otherPos.x - x).absoluteValue + (otherPos.y - y).absoluteValue + (otherPos.z - z).absoluteValue

    companion object {
        fun of(input: String): Point3D = input
            .removeSurrounding("<", ">")
            .removeSurrounding("(", ")")
            .removeSurrounding("[", "]")
            .removeSurrounding("{", "}")
            .split(",").run { Point3D(this[0].trim().toInt(), this[1].trim().toInt(), this[2].trim().toInt()) }

        val origin = Point3D(0,0,0)
    }

}

