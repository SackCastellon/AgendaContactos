package agenda.data.table

import agenda.model.Phone.Label
import org.jetbrains.exposed.sql.Table

internal object Phones : Table() {
    @JvmField val id = integer("id").autoIncrement().primaryKey()
    @JvmField val phone = varchar("phone", 15)
    @JvmField val label = enumeration("label", Label::class)
}
