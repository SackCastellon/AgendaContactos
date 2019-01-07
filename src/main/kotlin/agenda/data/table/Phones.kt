package agenda.data.table

import agenda.model.Phone
import agenda.model.Phone.Label
import agenda.util.PHONE_LENGTH
import org.jetbrains.exposed.sql.ResultRow

internal object Phones : IdTable<Phone>() {
    @JvmField val phone = varchar("phone", PHONE_LENGTH)
    @JvmField val label = enumeration("label", Label::class)

    override fun ResultRow.toData(): Phone = Phone.create(
        id = this[Phones.id],
        phone = this[Phones.phone],
        label = this[Phones.label]
    )
}
