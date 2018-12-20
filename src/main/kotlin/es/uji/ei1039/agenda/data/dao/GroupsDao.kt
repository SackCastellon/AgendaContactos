package es.uji.ei1039.agenda.data.dao

import es.uji.ei1039.agenda.data.DatabaseManager.dbQuery
import es.uji.ei1039.agenda.data.table.Groups
import es.uji.ei1039.agenda.model.Group
import org.jetbrains.exposed.sql.*

object GroupsDao : IDao<Group> {

    private val default: List<Group> = listOf(
        Group.New().apply { name = "Familia" },
        Group.New().apply { name = "Amigos" },
        Group.New().apply { name = "Trabajo" }
    )

    override fun add(item: Group): Int {
        require(item is Group.New)
        return dbQuery {
            Groups.insert {
                it[name] = item.name
            }.generatedKey as Int
        }
    }

    override fun get(id: Int): Group? {
        require(id >= 0)
        return dbQuery { Groups.select { Groups.id eq id } }.singleOrNull()?.toGroup()
    }

    override fun getAll(): List<Group> = dbQuery { Groups.selectAll() }.map { it.toGroup() }

    override fun remove(id: Int) {
        require(id >= 0)
        dbQuery { Groups.deleteWhere { Groups.id eq id } }
    }
}

internal fun ResultRow.toGroup(): Group = Group.Existing(
    id = this[Groups.id],
    name = this[Groups.name]
)
