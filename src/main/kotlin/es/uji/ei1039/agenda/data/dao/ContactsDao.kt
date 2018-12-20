package es.uji.ei1039.agenda.data.dao

import es.uji.ei1039.agenda.data.DatabaseManager.dbQuery
import es.uji.ei1039.agenda.data.table.*
import es.uji.ei1039.agenda.model.Contact
import es.uji.ei1039.agenda.model.Email
import es.uji.ei1039.agenda.model.Group
import es.uji.ei1039.agenda.model.Phone
import org.jetbrains.exposed.sql.*

object ContactsDao : IDao<Contact> {

    override fun add(item: Contact): Int {
        require(item is Contact.New)
        return dbQuery {
            val id = Contacts.insert {
                it[name] = item.name
                it[surname] = item.surname
            }.generatedKey as Int

            ContactPhones.batchInsert(item.phones) { phone ->
                let {
                    with(ContactPhones) {
                        it[contactId] = id
                        it[phoneId] = phone.id
                    }
                }
            }

            ContactEmails.batchInsert(item.emails) { email ->
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
    }
}

private fun ResultRow.toContact(phones: List<Phone>, emails: List<Email>, groups: List<Group>): Contact = Contact.Existing(
    id = this[Contacts.id],
    name = this[Contacts.name],
    surname = this[Contacts.surname],
    phones = phones,
    emails = emails,
    groups = groups
)
