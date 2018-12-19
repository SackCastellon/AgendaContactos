package es.uji.ei1039.agenda.data.table

import org.jetbrains.exposed.sql.Table

object Groups : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
}
