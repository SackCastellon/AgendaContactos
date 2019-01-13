package agenda.data.table

import agenda.model.Group
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow

object Groups : IdTable<Group>() {
    val name: Column<String> = varchar("name", GROUP_LENGTH)

    override fun ResultRow.toData(): Group = Group(
        id = this[id],
        name = this[name]
    )
}
