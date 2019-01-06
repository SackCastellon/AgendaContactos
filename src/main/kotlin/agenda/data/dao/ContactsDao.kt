package agenda.data.dao

import agenda.data.DatabaseManager.dbQuery
import agenda.data.table.*
import agenda.model.Contact
import agenda.model.Email
import agenda.model.Phone
import org.jetbrains.exposed.sql.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import tornadofx.observable

object ContactsDao : AbstractDao<Contact>(Contacts), KoinComponent {

    private val phonesDao: IDao<Phone> by inject("phones")
    private val emailsDao: IDao<Email> by inject("emails")

    override fun add(item: Contact): Contact {
        val id = dbQuery {
            if (!item.isNew) {
                Phones.deleteWhere {
                    Phones.id inList ContactPhones.slice(ContactPhones.phoneId)
                        .select { ContactPhones.contactId eq item.id }
                        .map { it[ContactPhones.phoneId] }
                        .minus(item.phones.map { it.id })
                }
                phonesDao.invalidate()

                Emails.deleteWhere {
                    Emails.id inList ContactEmails.slice(ContactEmails.emailId)
                        .select { ContactEmails.contactId eq item.id }
                        .map { it[ContactEmails.emailId] }
                        .minus(item.emails.map { it.id })
                }
                emailsDao.invalidate()

                ContactGroups.deleteWhere { ContactGroups.contactId eq item.id }
            }

            val id = if (item.isNew) {
                Contacts.insert {
                    it[firstName] = item.firstName
                    it[lastName] = item.lastName
                }.generatedKey as Int
            } else {
                Contacts.update({ Contacts.id eq item.id }) {
                    it[firstName] = item.firstName
                    it[lastName] = item.lastName
                }
                item.id
            }

            val newPhones = item.phones.mapNotNull { phone -> phonesDao.add(phone).takeIf { phone.isNew } }
            ContactPhones.batchInsert(newPhones) { phone ->
                let {
                    with(ContactPhones) {
                        it[contactId] = id
                        it[phoneId] = phone.id
                    }
                }
            }

            val newEmails = item.emails.mapNotNull { email -> emailsDao.add(email).takeIf { email.isNew } }
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
        invalidate()
        return get(id) ?: throw NoSuchElementException("Cannot find email with id: $id")
    }

    override fun get(id: Int): Contact? {
        require(id >= 0)
        return dbQuery {
            super.get(id)?.apply {
                phones = with(Phones) { innerJoin(ContactPhones).select { ContactPhones.contactId eq id }.map { it.toData() } }.observable()
                emails = with(Emails) { innerJoin(ContactEmails).select { ContactEmails.contactId eq id }.map { it.toData() } }.observable()
                groups = with(Groups) { innerJoin(ContactGroups).select { ContactGroups.contactId eq id }.map { it.toData() } }.observable()
            }
        }
    }

    override fun getAll(): List<Contact> {
        return dbQuery {
            val phones =
                with(Phones) { innerJoin(ContactPhones).selectAll().groupBy { it[ContactPhones.contactId] }.mapValues { it.value.map { it.toData() } } }

            val emails =
                with(Emails) { innerJoin(ContactEmails).selectAll().groupBy { it[ContactEmails.contactId] }.mapValues { it.value.map { it.toData() } } }

            val groups =
                with(Groups) { innerJoin(ContactGroups).selectAll().groupBy { it[ContactGroups.contactId] }.mapValues { it.value.map { it.toData() } } }

            super.getAll().onEach {
                it.phones = phones[it.id].orEmpty().observable()
                it.emails = emails[it.id].orEmpty().observable()
                it.groups = groups[it.id].orEmpty().observable()
            }
        }
    }
}
