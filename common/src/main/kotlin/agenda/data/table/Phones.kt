package agenda.data.table

import agenda.model.Phone
import agenda.model.Phone.Label
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow

object Phones : IdTable<Phone>() {
    val phone: Column<String> = varchar("phone", PHONE_LENGTH)
    val label: Column<Label> = enumeration("label", Label::class)

    override fun ResultRow.toData(): Phone = Phone(
        id = this[id],
        phone = this[phone],
        label = this[label]
    )
}
