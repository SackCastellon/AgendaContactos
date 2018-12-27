package agenda.data.table

import agenda.model.Email.Label
import org.jetbrains.exposed.sql.Table

internal object Emails : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val email = varchar("email", 255)
    val label = enumeration("label", Label::class)
}
