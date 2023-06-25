package tool.coordinate.twodimensional

enum class Direction(val directionChar: Char) {
    UP('^') {
        override fun rotateRight() = RIGHT
        override fun rotateLeft() = LEFT
    },
    DOWN('v') {
        override fun rotateRight() = LEFT
        override fun rotateLeft() = RIGHT
    },
    RIGHT('>') {
        override fun rotateRight() = DOWN
        override fun rotateLeft() = UP
    },
    LEFT('<') {
        override fun rotateRight() = UP
        override fun rotateLeft() = DOWN
    };

    abstract fun rotateRight(): Direction
    abstract fun rotateLeft(): Direction
    override fun toString() = directionChar.toString()
    fun opposite() = rotateLeft().rotateLeft()
}
