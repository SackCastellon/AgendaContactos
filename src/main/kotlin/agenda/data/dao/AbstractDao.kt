package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.IdTable
import agenda.model.IData
import javafx.beans.binding.ListBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import tornadofx.observable

abstract class AbstractDao<T : IData>(private val table: IdTable<T>) : IDao<T> {
    private val listBinding: ListBinding<T> by lazy {
        object : ListBinding<T>() {
            override fun computeValue(): ObservableList<T> = getAll().observable()
        }
    }

    final override val observable: ObservableList<T> get() = FXCollections.unmodifiableObservableList(listBinding)

    final override fun invalidate(): Unit = listBinding.invalidate()

    override fun get(id: Int): T? {
        require(id >= 0)
        return dbQuery { with(table) { select { this@with.id eq id }.singleOrNull()?.toData() } }
    }

    override fun getAll(): List<T> {
        return dbQuery { with(table) { selectAll().map { it.toData() } } }
    }

    override fun remove(id: Int) {
        require(id >= 0)
        dbQuery { with(table) { deleteWhere { this@with.id eq id } } }.also { if (it > 0) invalidate() }
    }
}
