package es.uji.ei1039.agenda.data.dao

import javafx.collections.ObservableList

interface IDao<T> {
    val observable: ObservableList<T>

    fun add(item: T): T
    @JvmDefault
    fun addAll(items: List<T>): List<T> = items.map { add(it) }

    operator fun get(id: Int): T?
    fun getAll(): List<T>
    fun remove(id: Int)
}

fun <T> IDao<T>.addAll(vararg items: T): List<T> = addAll(items.asList())
