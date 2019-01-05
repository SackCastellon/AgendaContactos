package agenda.data.table

import agenda.model.Email.Label
import org.jetbrains.exposed.sql.Table

internal object Emails : Table() {
    @JvmField val id = integer("id").autoIncrement().primaryKey()
    @JvmField val email = varchar("email", 255)
    @JvmField val label = enumeration("label", Label::class)
}
