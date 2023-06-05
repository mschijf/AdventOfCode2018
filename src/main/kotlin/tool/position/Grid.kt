package tool.position

fun <T> Map<Coordinate, T>.printAsGrid(default: String=".", itemAsString: (T)->String) {
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

fun Set<Coordinate>.printAsGrid(defaultEmpty: String=".", defaultAvailable: String="#") {
    val minX = this.minByOrNull { it.x }?.x ?: -1
    val minY = this.minByOrNull { it.y }?.y ?: -1
    val maxX = this.maxByOrNull { it.x }?.x ?: -1
    val maxY = this.maxByOrNull { it.y }?.y ?: -1

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            val field = Coordinate(x,y)
            if (this.contains(field)) {
                print(defaultAvailable)
            } else {
                print(defaultEmpty)
            }
        }
        println()
    }
}


fun List<Coordinate>.area(): Long {
    if (this.isEmpty())
        return 0
    val minX = this.minOf { it.x }
    val minY = this.minOf { it.y }
    val maxX = this.maxOf { it.x }
    val maxY = this.maxOf { it.y }
    return (maxX - minX + 1L) * (maxY - minY + 1L)
}
