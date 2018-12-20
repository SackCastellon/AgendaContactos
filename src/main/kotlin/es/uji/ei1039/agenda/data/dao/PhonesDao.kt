package es.uji.ei1039.agenda.data.dao

import es.uji.ei1039.agenda.data.DatabaseManager.dbQuery
import es.uji.ei1039.agenda.data.table.Phones
import es.uji.ei1039.agenda.model.Phone
import org.jetbrains.exposed.sql.*

object PhonesDao : IDao<Phone> {

    override fun add(item: Phone): Int {
        require(item is Phone.New)
        return dbQuery {
            Phones.insert {
                it[phone] = item.phone
                it[label] = item.label
            }.generatedKey as Int
        }
    }

    override fun get(id: Int): Phone? {
        require(id >= 0)
        return dbQuery { Phones.select { Phones.id eq id } }.singleOrNull()?.toPhone()
    }

    override fun getAll(): List<Phone> = dbQuery { Phones.selectAll() }.map { it.toPhone() }

    override fun remove(id: Int) {
        require(id >= 0)
        dbQuery { Phones.deleteWhere { Phones.id eq id } }
    }
}

internal fun ResultRow.toPhone(): Phone = Phone.Existing(
    id = this[Phones.id],
    phone = this[Phones.phone],
    label = this[Phones.label]
)
