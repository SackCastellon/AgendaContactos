package agenda.data.dao

import javafx.collections.ObservableList

interface IDao<T> {
    val observable: ObservableList<T>
    fun invalidate()

    // Add data

    fun add(item: T): T

    // Get data

    operator fun get(id: Int): T?
    fun getAll(): List<T>

    // Delete data

    fun remove(id: Int)
}
