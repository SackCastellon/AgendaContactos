package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.Groups
import agenda.model.Group
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

object GroupsDao : AbstractDao<Group>(Groups) {

    override fun add(item: Group): Group {
        val id = dbQuery {
            if (item.isNew) {
                Groups.insert {
                    it[name] = item.name
                }.generatedKey as Int
            } else {
                Groups.update({ Groups.id eq item.id }) {
                    it[name] = item.name
                }
                item.id
            }
        }
        invalidate()
        return get(id) ?: throw NoSuchElementException("Cannot find group with id: $id")
    }

    override fun remove(id: Int) {
        require(id >= 0)
        dbQuery {
            Groups.deleteWhere { (Groups.id eq id) and (Groups.name notInList Group.defaults) }
        }.also {
            if (it > 0) invalidate()
        }
    }
}
