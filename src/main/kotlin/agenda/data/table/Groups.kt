package agenda.data.table

import org.jetbrains.exposed.sql.Table

internal object Groups : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
}
