package es.uji.ei1039.agenda.data.dao

import es.uji.ei1039.agenda.data.DatabaseManager.dbQuery
import es.uji.ei1039.agenda.data.table.Emails
import es.uji.ei1039.agenda.model.Email
import javafx.beans.binding.ListBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.jetbrains.exposed.sql.*
import tornadofx.observable

object EmailsDao : IDao<Email> {

    private val emails by lazy {
        object : ListBinding<Email>() {
            override fun computeValue(): ObservableList<Email> = getAll().observable()
        }
    }

    override val observable: ObservableList<Email>
        get() = FXCollections.unmodifiableObservableList(emails)

    override fun add(item: Email): Email {
        val id = if (item.isNew) dbQuery {
            Emails.insert {
                it[email] = item.email
                it[label] = item.label
            }.generatedKey as Int
        } else dbQuery {
            Emails.update({ Emails.id eq item.id }) {
                it[email] = item.email
                it[label] = item.label
            }
            item.id
        }
        return get(id) ?: throw NoSuchElementException("Cannot find email with id: $id")
    }

    override fun get(id: Int): Email? = dbQuery { Emails.select { Emails.id eq id }.singleOrNull()?.toEmail() }
    override fun getAll(): List<Email> = dbQuery { Emails.selectAll().map { it.toEmail() } }
    override fun remove(id: Int): Unit = dbQuery { Emails.deleteWhere { Emails.id eq id } }
}

internal fun ResultRow.toEmail(): Email = Email.create(
    id = this[Emails.id],
    email = this[Emails.email],
    label = this[Emails.label]
)
