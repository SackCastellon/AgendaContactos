package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.Phones
import agenda.model.Phone
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

object PhonesDao : AbstractDao<Phone>(Phones) {

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
}
