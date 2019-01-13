package agenda.data.table

import agenda.model.Email
import agenda.model.Email.Label
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow

object Emails : IdTable<Email>() {
    val email: Column<String> = varchar("email", EMAIL_LENGTH)
    val label: Column<Label> = enumeration("label", Label::class)

    override fun ResultRow.toData(): Email = Email(
        id = this[id],
        email = this[email],
        label = this[label]
    )
}
