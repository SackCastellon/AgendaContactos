package es.uji.ei1039.agenda.data

import javafx.collections.ObservableList

interface IRepository<T> {
    fun getAll(): ObservableList<T>
    fun getById(id: Int): T?
    fun add(item: T)
    fun remove(id: Int)
    fun remove(item: T)
}
