package es.uji.ei1039.agenda.data.dao

import es.uji.ei1039.agenda.data.DatabaseManager.dbQuery
import es.uji.ei1039.agenda.data.table.Phones
import es.uji.ei1039.agenda.model.Phone
import javafx.beans.binding.ListBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.jetbrains.exposed.sql.*
import tornadofx.observable

object PhonesDao : IDao<Phone> {

    private val phones by lazy {
        object : ListBinding<Phone>() {
            override fun computeValue(): ObservableList<Phone> = getAll().observable()
        }
    }

    override val observable: ObservableList<Phone>
        get() = FXCollections.unmodifiableObservableList(phones)

    override fun add(item: Phone): Phone {
        val id = if (item.isNew) dbQuery {
            Phones.insert {
                it[phone] = item.phone
                it[label] = item.label
            }.generatedKey as Int
        } else dbQuery {
            Phones.update({ Phones.id eq item.id }) {
                it[phone] = item.phone
                it[label] = item.label
            }
            item.id
        }
        return get(id) ?: throw NoSuchElementException("Cannot find phone with id: $id")
    }

    override fun get(id: Int): Phone? = dbQuery { Phones.select { Phones.id eq id }.singleOrNull()?.toPhone() }
    override fun getAll(): List<Phone> = dbQuery { Phones.selectAll().map { it.toPhone() } }
    override fun remove(id: Int): Unit = dbQuery { Phones.deleteWhere { Phones.id eq id } }
}

internal fun ResultRow.toPhone(): Phone = Phone.create(
    id = this[Phones.id],
    phone = this[Phones.phone],
    label = this[Phones.label]
)
