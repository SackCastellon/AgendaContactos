package agenda.data.table

import agenda.model.Email
import agenda.model.Email.Label
import agenda.util.EMAIL_LENGTH
import org.jetbrains.exposed.sql.ResultRow

internal object Emails : IdTable<Email>() {
    @JvmField val email = varchar("email", EMAIL_LENGTH)
    @JvmField val label = enumeration("label", Label::class)

    override fun ResultRow.toData(): Email = Email.create(
        id = this[Emails.id],
        email = this[Emails.email],
        label = this[Emails.label]
    )
}
