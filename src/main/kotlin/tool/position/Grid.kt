package tool.position

fun <T> Map<Coordinate, T>.print(default: String="NULL", itemAsString: (T)->String) {
    val maxX = this.keys.maxByOrNull { it.x }?.x ?: -1
    val maxY = this.keys.maxByOrNull { it.y }?.y ?: -1

    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            val field = Coordinate(x,y)
            if (this.contains(field)) {
                print(itemAsString(this[field]!!))
            } else {
                print(default)
            }
        }
        println()
    }
}
