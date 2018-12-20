package es.uji.ei1039.agenda.data.dao

import es.uji.ei1039.agenda.data.DatabaseManager.dbQuery
import es.uji.ei1039.agenda.data.table.Emails
import es.uji.ei1039.agenda.model.Email
import org.jetbrains.exposed.sql.*

object EmailsDao : IDao<Email> {

    override fun add(item: Email): Int {
        require(item is Email.New)
        return dbQuery {
            Emails.insert {
                it[email] = item.email
                it[label] = item.label
            }.generatedKey as Int
        }
    }

    override fun get(id: Int): Email? {
        require(id >= 0)
        return dbQuery { Emails.select { Emails.id eq id } }.singleOrNull()?.toEmail()
    }

    override fun getAll(): List<Email> = dbQuery { Emails.selectAll() }.map { it.toEmail() }

    override fun remove(id: Int) {
        require(id >= 0)
        dbQuery { Emails.deleteWhere { Emails.id eq id } }
    }
}

internal fun ResultRow.toEmail(): Email = Email.Existing(
    id = this[Emails.id],
    email = this[Emails.email],
    label = this[Emails.label]
)
