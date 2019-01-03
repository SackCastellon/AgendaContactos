package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.Emails
import agenda.model.Email
import org.jetbrains.exposed.sql.*

object EmailsDao : AbstractDao<Email>() {

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

    override fun get(id: Int): Email? {
        require(id >= 0)
        return dbQuery {
            Emails.select { Emails.id eq id }.singleOrNull()?.toEmail()
        }
    }

    override fun getAll(): List<Email> {
        return dbQuery {
            Emails.selectAll().map { it.toEmail() }
        }
    }

    override fun remove(id: Int) {
        require(id >= 0)
        dbQuery {
            Emails.deleteWhere { Emails.id eq id }
        }.also {
            if (it > 0) invalidate()
        }
    }
}

internal fun ResultRow.toEmail(): Email = Email.create(
    id = this[Emails.id],
    email = this[Emails.email],
    label = this[Emails.label]
)
