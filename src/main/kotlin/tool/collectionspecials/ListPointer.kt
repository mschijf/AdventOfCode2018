package tool.collectionspecials

interface ListPointer {
    operator fun plus(steps: Int): ListPointer
    operator fun minus(steps: Int): ListPointer
}

