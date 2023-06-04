package tool.collectionspecials

class CircularLinkedList<T : Any> : Iterable<T> {

    private var first: Node<T>? = null
    var size = 0
        private set

    fun isEmpty() = size == 0
    fun first() = first

    fun init(data: T): Node<T> {
        first = Node<T>(data)
        size = 1
        return first!!
    }

    fun insertAfter(after: Node<T>, data: T): Node<T> {
        val new = Node<T>(data, after, after.next)
        val tmpNext = new.next
        val tmpPrev = new.prev
        tmpNext.prev = new
        tmpPrev.next = new
        size++
        return new
    }

    fun remove(node: Node<T>) {
        node.prev.next = node.next
        node.next.prev = node.prev
        if (first == node) {
            first = node.next
        }
        size--
        if (size == 0) {
            first = null
        }
    }

    fun getNextNode(aNode: Node<T>, steps: Int=0): Node<T> {
        var current = aNode
        if (steps >= 0) {
            repeat(steps % size) {current = current.next}
        } else {
            repeat(-(steps % size)) {current = current.prev}
        }
        return current
    }

    fun getPreviousNode(aNode: Node<T>, steps: Int=0): Node<T> {
        return getNextNode(aNode, -steps)
    }

    override fun toString() = this.joinToString(" ")

    override fun iterator(): Iterator<T>  = CycledLinkedListIterator(this)

    inner class Node<T>(val data: T, parPrev: Node<T>?=null, parNext: Node<T>?=null) {

        var prev: Node<T> = parPrev ?: this
        var next: Node<T> = parNext ?: this

        override fun toString() = data.toString()
    }

    inner class CycledLinkedListIterator<T: Any>(private val cll: CircularLinkedList<T>): Iterator<T> {
        private var current = cll.first()
        private var neverIterated = true

        override fun hasNext(): Boolean {
            if (cll.isEmpty())
                return false
            if (neverIterated)
                return true
            return current !== cll.first()
        }

        override fun next(): T {
            if (!hasNext())
                throw Exception("No next on CycledLinkedList iterator")
            neverIterated = false
            val data = current?.data!!
            current = current?.next
            return data
        }
    }
}


