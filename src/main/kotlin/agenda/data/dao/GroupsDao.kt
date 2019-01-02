package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.Groups
import agenda.model.Group
import javafx.beans.binding.ListBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.jetbrains.exposed.sql.*
import tornadofx.observable

object GroupsDao : IDao<Group> {

    private val listBinding = object : ListBinding<Group>() {
        override fun computeValue(): ObservableList<Group> = getAll().observable()
    }

    override val observable: ObservableList<Group> = FXCollections.unmodifiableObservableList(listBinding)

    override fun add(item: Group): Group {
        val id = if (item.isNew) dbQuery {
            Groups.insert {
                it[name] = item.name
            }.generatedKey as Int
        } else dbQuery {
            Groups.update({ Groups.id eq item.id }) {
                it[name] = item.name
            }
            item.id
        }
        listBinding.invalidate()
        return get(id) ?: throw NoSuchElementException("Cannot find group with id: $id")
    }

    override fun get(id: Int): Group? {
        require(id >= 0)
        return dbQuery {
            Groups.select { Groups.id eq id }.singleOrNull()?.toGroup()
        }
    }

    override fun getAll(): List<Group> {
        return dbQuery {
            Groups.selectAll().map { it.toGroup() }
        }
    }

    override fun remove(id: Int) {
        require(id >= 0)
        dbQuery {
            Groups.deleteWhere { (Groups.id eq id) and (Groups.name notInList Group.defaults) }
        }.also {
            if (it > 0) listBinding.invalidate()
        }
    }
}

internal fun ResultRow.toGroup(): Group = Group.create(
    id = this[Groups.id],
    name = this[Groups.name]
)
