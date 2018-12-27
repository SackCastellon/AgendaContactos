package agenda.data.table

import agenda.model.Phone.Label
import org.jetbrains.exposed.sql.Table

internal object Phones : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val phone = varchar("phone", 15)
    val label = enumeration("label", Label::class)
}
