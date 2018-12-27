package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.*
import agenda.model.Contact
import agenda.model.Email
import agenda.model.Group
import agenda.model.Phone
import javafx.beans.binding.ListBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.jetbrains.exposed.sql.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import tornadofx.observable

object ContactsDao : IDao<Contact>, KoinComponent {

    private val phonesDao: IDao<Phone> by inject("phones")
    private val emailsDao: IDao<Email> by inject("emails")

    private val contacts by lazy {
        object : ListBinding<Contact>() {
            override fun computeValue(): ObservableList<Contact> = getAll().observable()
        }
    }

    override val observable: ObservableList<Contact>
        get() = FXCollections.unmodifiableObservableList(contacts)

    override fun add(item: Contact): Contact {
        val id = if (item.isNew) {

            val newPhones = phonesDao.addAll(item.phones)
            val newEmails = emailsDao.addAll(item.emails)

            dbQuery {
                val id = Contacts.insert {
                    it[firstName] = item.firstName
                    it[lastName] = item.lastName
                }.generatedKey as Int

                ContactPhones.batchInsert(newPhones) { phone ->
                    let {
                        with(ContactPhones) {
                            it[contactId] = id
                            it[phoneId] = phone.id
                        }
                    }
                }

                ContactEmails.batchInsert(newEmails) { email ->
                    let {
                        with(ContactEmails) {
                            it[contactId] = id
                            it[emailId] = email.id
                        }
                    }
                }

                ContactGroups.batchInsert(item.groups) { group ->
                    let {
                        with(ContactGroups) {
                            it[contactId] = id
                            it[groupId] = group.id
                        }
                    }
                }

                id
            }
        } else dbQuery {
            Contacts.update({ Contacts.id eq item.id }) {
                it[firstName] = item.firstName
                it[lastName] = item.lastName
            }

            item.id
        }
        contacts.invalidate()
        return get(id) ?: throw NoSuchElementException("Cannot find email with id: $id")
    }

    override fun get(id: Int): Contact? {
        require(id >= 0)
        return dbQuery {
            val phones = Phones.innerJoin(ContactPhones).select { ContactPhones.contactId eq id }.map { it.toPhone() }
            val emails = Emails.innerJoin(ContactEmails).select { ContactEmails.contactId eq id }.map { it.toEmail() }
            val groups = Groups.innerJoin(ContactGroups).select { ContactGroups.contactId eq id }.map { it.toGroup() }

            Contacts.select { Contacts.id eq id }.firstOrNull()?.toContact(phones, emails, groups)
        }
    }

    override fun getAll(): List<Contact> {
        return dbQuery {
            val phones = Phones.innerJoin(ContactPhones).selectAll()
                .groupBy { it[ContactPhones.contactId] }.mapValues { (_, v) -> v.map { it.toPhone() } }
            val emails = Emails.innerJoin(ContactEmails).selectAll()
                .groupBy { it[ContactEmails.contactId] }.mapValues { (_, v) -> v.map { it.toEmail() } }
            val groups = Groups.innerJoin(ContactGroups).selectAll()
                .groupBy { it[ContactPhones.contactId] }.mapValues { (_, v) -> v.map { it.toGroup() } }

            Contacts.selectAll().map {
                val id = it[Contacts.id]
                it.toContact(phones[id].orEmpty(), emails[id].orEmpty(), groups[id].orEmpty())
            }
        }
    }

    override fun remove(id: Int) {
        require(id >= 0)
        dbQuery {
            Contacts.deleteWhere { Contacts.id eq id }
        }
        contacts.invalidate()
    }
}

private fun ResultRow.toContact(phones: List<Phone>, emails: List<Email>, groups: List<Group>): Contact = Contact.create(
    id = this[Contacts.id],
    firstName = this[Contacts.firstName],
    lastName = this[Contacts.lastName],
    phones = phones,
    emails = emails,
    groups = groups
)
