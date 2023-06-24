package tool.collectionspecials

interface ICircularLinkedList<T> : Iterable<T> {
    fun isEmpty(): Boolean
    fun add(element: T): ListPointer
    fun add(pos: ListPointer, element: T): ListPointer
    fun removeAt(p: ListPointer): Boolean
    operator fun get(p: ListPointer): T
}

fun <T>emptyCircularList(): ICircularLinkedList<T> {
    return CircularLinkedList<T>()
}

class CircularLinkedList<T> : ICircularLinkedList<T> {

    private var first: Node? = null
    fun firstOrNull(): ListPointer? = first

    var size = 0
        private set

    override fun isEmpty() = size == 0

    override fun add(element: T): ListPointer {
        return if (first == null) {
            addFirst(element)
        } else {
            add(firstOrNull()!!, element)
        }
    }

    private fun addFirst(element: T): ListPointer {
        size++
        first = Node(element)
        return first!!
    }

    override fun add(pos: ListPointer, element: T): ListPointer {
        val node = pos.toNode()
        val new = Node(element, node.prev, node)
        val tmpPrev = new.prev
        val tmpNext = new.next
        tmpPrev.next = new
        tmpNext.prev = new
        size++
        return new
    }

    override fun removeAt(p: ListPointer): Boolean {
        val nodeToBeRemoved = p.toNode()
        nodeToBeRemoved.prev.next = nodeToBeRemoved.next
        nodeToBeRemoved.next.prev = nodeToBeRemoved.prev
        if (first == nodeToBeRemoved) {
            first = nodeToBeRemoved.next
        }
        size--
        if (size == 0) {
            first = null
        }
        return true
    }

    override fun get(p: ListPointer): T {
        return p.toNode().data
    }

    private fun ListPointer.toNode() = (this as CircularLinkedList<T>.Node)

    override fun toString() = this.joinToString(" ")

    private inner class Node(val data: T, pprev: Node?=null, pnext: Node?=null): ListPointer {
        var prev: Node = pprev ?: this
        var next: Node = pnext ?: this

        override fun plus(steps: Int): ListPointer {
            var current = this
            if (steps >= 0) {
                repeat(steps % size) { current = current.next }
            } else {
                repeat(-(steps % size)) { current = current.prev }
            }
            return current
        }

        override fun minus(steps: Int): ListPointer {
            return plus(-steps)
        }

        override fun toString() = data.toString()
    }

    override fun iterator(): Iterator<T>  = CircularLinkedListIterator(this)

    inner class CircularLinkedListIterator(private val cll: CircularLinkedList<T>): Iterator<T> {
        private var current = firstOrNull()?.toNode()
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
            val data = current!!.data
            current = current!!.next
            return data
        }
    }
}


