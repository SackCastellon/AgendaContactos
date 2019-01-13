package agenda.data.table

import agenda.model.Data
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

abstract class IdTable<T : Data<T>>(name: String = "") : Table(name) {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()

    abstract fun ResultRow.toData(): T
}
