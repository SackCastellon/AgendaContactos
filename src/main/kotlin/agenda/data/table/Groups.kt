package agenda.data.table

import org.jetbrains.exposed.sql.Table

internal object Groups : Table() {
    @JvmField val id = integer("id").autoIncrement().primaryKey()
    @JvmField val name = varchar("name", 100)
}
