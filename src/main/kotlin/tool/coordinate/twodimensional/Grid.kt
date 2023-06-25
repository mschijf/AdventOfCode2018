package tool.coordinate.twodimensional

fun <T> Map<Pos, T>.printAsGrid(default: String=".", itemAsString: (T)->String) {
    val maxX = this.keys.maxByOrNull { it.x }?.x ?: -1
    val maxY = this.keys.maxByOrNull { it.y }?.y ?: -1

    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            val field = Pos(x,y)
            if (this.contains(field)) {
                print(itemAsString(this[field]!!))
            } else {
                print(default)
            }
        }
        println()
    }
}

fun Collection<Pos>.printAsGrid(itemAsString: (Pos)->String) {
    val minX = this.minByOrNull { it.x }?.x ?: -1
    val minY = this.minByOrNull { it.y }?.y ?: -1
    val maxX = this.maxByOrNull { it.x }?.x ?: -1
    val maxY = this.maxByOrNull { it.y }?.y ?: -1

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            val field = Pos(x,y)
            print(itemAsString(field))
        }
        println()
    }
}

fun Pair<Pos, Pos>.printGrid(itemAsString: (Pos)->String) {
    val minX = this.first.x
    val minY = this.first.y
    val maxX = this.second.x
    val maxY = this.second.y

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            val field = Pos(x,y)
            print(itemAsString(field))
        }
        println()
    }
}



fun Set<Pos>.printAsGrid(defaultEmpty: String=".", defaultAvailable: String="#") {
    val minX = this.minByOrNull { it.x }?.x ?: -1
    val minY = this.minByOrNull { it.y }?.y ?: -1
    val maxX = this.maxByOrNull { it.x }?.x ?: -1
    val maxY = this.maxByOrNull { it.y }?.y ?: -1

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            val field = Pos(x,y)
            if (this.contains(field)) {
                print(defaultAvailable)
            } else {
                print(defaultEmpty)
            }
        }
        println()
    }
}