package lv.n3o.aoc2021

class Node<T>(var value: T, nextInit: Node<T>? = null) {
    var next: Node<T> = nextInit ?: this

    fun removeNodeAfter(): Node<T> {
        val nextNode = next
        this.next = nextNode.next
        nextNode.next = nextNode
        return nextNode
    }

    fun removeAfter(): T {
        val nextValue = next.value
        this.next = this.next.next
        return nextValue
    }

    fun putNodeAfter(value: Node<T>): Node<T> {
        value.next = this.next
        this.next = value
        return value
    }

    fun putAfter(value: T): Node<T> {
        val newNode = Node(value, this.next)
        this.next = newNode
        return newNode
    }

    fun take(count: Int): List<T> {
        var countDown = count
        var nextNode = this
        return sequence {
            do {
                yield(nextNode.value)
                countDown--
                nextNode = nextNode.next
            } while (countDown > 0 && nextNode != this)
        }.toList()
    }

    fun allNodes() = sequence {
        var current = this@Node
        do {
            yield(current)
            current = current.next
        } while (current != this@Node)
    }
}