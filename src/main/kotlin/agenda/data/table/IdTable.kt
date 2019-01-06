package agenda.data.table

import agenda.model.IData
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

abstract class IdTable<T : IData>(name: String = "") : Table(name) {
    @JvmField val id: Column<Int> = integer("id").autoIncrement().primaryKey()

    abstract fun ResultRow.toData(): T
}
