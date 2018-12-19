package es.uji.ei1039.agenda.data.table

import es.uji.ei1039.agenda.model.Phone.Label
import org.jetbrains.exposed.sql.Table

object Phones : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val phone = varchar("phone", 15)
    val label = enumeration("label", Label::class)
}
