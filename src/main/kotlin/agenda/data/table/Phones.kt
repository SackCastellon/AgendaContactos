package agenda.data.table

import agenda.model.Phone
import agenda.model.Phone.Label
import org.jetbrains.exposed.sql.ResultRow

internal object Phones : IdTable<Phone>() {
    @JvmField val phone = varchar("phone", 15)
    @JvmField val label = enumeration("label", Label::class)

    override fun ResultRow.toData(): Phone = Phone.create(
        id = this[Phones.id],
        phone = this[Phones.phone],
        label = this[Phones.label]
    )
}
