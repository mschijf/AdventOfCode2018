package tool.coordinatesystem

fun <T> Map<GridPos, T>.printAsGrid(default: String=".", itemAsString: (T)->String) {
    val maxX = this.keys.maxByOrNull { it.x }?.x ?: -1
    val maxY = this.keys.maxByOrNull { it.y }?.y ?: -1

    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            val field = GridPos(x,y)
            if (this.contains(field)) {
                print(itemAsString(this[field]!!))
            } else {
                print(default)
            }
        }
        println()
    }
}

fun Collection<GridPos>.printAsGrid(itemAsString: (GridPos)->String) {
    val minX = this.minByOrNull { it.x }?.x ?: -1
    val minY = this.minByOrNull { it.y }?.y ?: -1
    val maxX = this.maxByOrNull { it.x }?.x ?: -1
    val maxY = this.maxByOrNull { it.y }?.y ?: -1

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            val field = GridPos(x,y)
            print(itemAsString(field))
        }
        println()
    }
}

fun Pair<GridPos, GridPos>.printGrid(itemAsString: (GridPos)->String) {
    val minX = this.first.x
    val minY = this.first.y
    val maxX = this.second.x
    val maxY = this.second.y

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            val field = GridPos(x,y)
            print(itemAsString(field))
        }
        println()
    }
}



fun Set<GridPos>.printAsGrid(defaultEmpty: String=".", defaultAvailable: String="#") {
    val minX = this.minByOrNull { it.x }?.x ?: -1
    val minY = this.minByOrNull { it.y }?.y ?: -1
    val maxX = this.maxByOrNull { it.x }?.x ?: -1
    val maxY = this.maxByOrNull { it.y }?.y ?: -1

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            val field = GridPos(x,y)
            if (this.contains(field)) {
                print(defaultAvailable)
            } else {
                print(defaultEmpty)
            }
        }
        println()
    }
}


fun List<GridPos>.area(): Long {
    if (this.isEmpty())
        return 0
    val minX = this.minOf { it.x }
    val minY = this.minOf { it.y }
    val maxX = this.maxOf { it.x }
    val maxY = this.maxOf { it.y }
    return (maxX - minX + 1L) * (maxY - minY + 1L)
}

//fun List<Coordinate>.area(): Long {
//    if (this.isEmpty())
//        return 0
//    val minX = this.minOf { it.x }
//    val minY = this.minOf { it.y }
//    val maxX = this.maxOf { it.x }
//    val maxY = this.maxOf { it.y }
//    return (maxX - minX + 1L) * (maxY - minY + 1L)
//}

