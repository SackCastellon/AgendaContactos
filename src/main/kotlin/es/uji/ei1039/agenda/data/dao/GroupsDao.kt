package es.uji.ei1039.agenda.data.dao

import es.uji.ei1039.agenda.data.DatabaseManager.dbQuery
import es.uji.ei1039.agenda.data.table.Groups
import es.uji.ei1039.agenda.model.Group
import javafx.beans.binding.ListBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.jetbrains.exposed.sql.*
import tornadofx.observable

object GroupsDao : IDao<Group> {

    private val groups by lazy {
        object : ListBinding<Group>() {
            override fun computeValue(): ObservableList<Group> = getAll().observable()
        }
    }

    override val observable: ObservableList<Group>
        get() = FXCollections.unmodifiableObservableList(groups)

    private val default: List<Group> = listOf(
        Group.empty().apply { name = "Familia" },
        Group.empty().apply { name = "Amigos" },
        Group.empty().apply { name = "Trabajo" }
    )

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
        return get(id) ?: throw NoSuchElementException("Cannot find group with id: $id")
    }

    override fun get(id: Int): Group? = dbQuery { Groups.select { Groups.id eq id }.singleOrNull()?.toGroup() }
    override fun getAll(): List<Group> = dbQuery { Groups.selectAll().map { it.toGroup() } }
    override fun remove(id: Int): Unit = dbQuery { Groups.deleteWhere { Groups.id eq id } }
}

internal fun ResultRow.toGroup(): Group = Group.create(
    id = this[Groups.id],
    name = this[Groups.name]
)
