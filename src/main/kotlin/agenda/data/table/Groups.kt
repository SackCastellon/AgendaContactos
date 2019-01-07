package agenda.data.table

import agenda.model.Group
import agenda.util.GROUP_LENGTH
import org.jetbrains.exposed.sql.ResultRow

internal object Groups : IdTable<Group>() {
    @JvmField val name = varchar("name", GROUP_LENGTH)

    override fun ResultRow.toData(): Group = Group.create(
        id = this[Groups.id],
        name = this[Groups.name]
    )
}
