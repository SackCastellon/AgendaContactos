package agenda.data.table

import agenda.model.Group
import agenda.util.NAME_LENGTH
import org.jetbrains.exposed.sql.ResultRow

internal object Groups : IdTable<Group>() {
    @JvmField val name = varchar("name", NAME_LENGTH)

    override fun ResultRow.toData(): Group = Group.create(
        id = this[Groups.id],
        name = this[Groups.name]
    )
}
