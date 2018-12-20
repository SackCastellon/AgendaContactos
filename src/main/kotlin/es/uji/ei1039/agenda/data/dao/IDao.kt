package es.uji.ei1039.agenda.data.dao

interface IDao<T> {
    fun add(item: T): Int
    fun get(id: Int): T?
    fun getAll(): List<T>
    fun remove(id: Int)
}
