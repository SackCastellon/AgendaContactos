package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.Emails
import agenda.model.Email
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

object EmailsDao : AbstractDao<Email>(Emails) {

    override fun add(item: Email): Email {
        val id = dbQuery {
            if (item.isNew) {
                Emails.insert {
                    it[email] = item.email
                    it[label] = item.label
                }.generatedKey as Int
            } else {
                Emails.update({ Emails.id eq item.id }) {
                    it[email] = item.email
                    it[label] = item.label
                }
                item.id
            }
        }
        invalidate()
        return get(id) ?: throw NoSuchElementException("Cannot find email with id: $id")
    }
}
