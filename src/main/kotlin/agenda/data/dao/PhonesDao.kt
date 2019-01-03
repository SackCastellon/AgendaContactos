package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.Phones
import agenda.model.Phone
import org.jetbrains.exposed.sql.*

object PhonesDao : AbstractDao<Phone>() {

    override fun add(item: Phone): Phone {
        val id = dbQuery {
            if (item.isNew) {
                Phones.insert {
                    it[phone] = item.phone
                    it[label] = item.label
                }.generatedKey as Int
            } else {
                Phones.update({ Phones.id eq item.id }) {
                    it[phone] = item.phone
                    it[label] = item.label
                }
                item.id
            }
        }
        invalidate()
        return get(id) ?: throw NoSuchElementException("Cannot find phone with id: $id")
    }

    override fun get(id: Int): Phone? {
        require(id >= 0)
        return dbQuery {
            Phones.select { Phones.id eq id }.singleOrNull()?.toPhone()
        }
    }

    override fun getAll(): List<Phone> {
        return dbQuery {
            Phones.selectAll().map { it.toPhone() }
        }
    }

    override fun remove(id: Int) {
        dbQuery {
            Phones.deleteWhere { Phones.id eq id }
        }.also {
            if (it > 0) invalidate()
        }
    }
}

internal fun ResultRow.toPhone(): Phone = Phone.create(
    id = this[Phones.id],
    phone = this[Phones.phone],
    label = this[Phones.label]
)
