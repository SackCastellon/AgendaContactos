package agenda.data.table

import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT
import org.jetbrains.exposed.sql.Table

internal object Contacts : Table() {
    @JvmField val id = integer("id").autoIncrement().primaryKey()
    @JvmField val firstName = varchar("first_name", 100)
    @JvmField val lastName = varchar("last_name", 100)
}

internal object ContactPhones : Table() {
    @JvmField val contactId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    @JvmField val phoneId = integer("phone_id").primaryKey().references(Phones.id, onDelete = CASCADE, onUpdate = RESTRICT)
}

internal object ContactEmails : Table() {
    @JvmField val contactId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    @JvmField val emailId = integer("email_id").primaryKey().references(Emails.id, onDelete = CASCADE, onUpdate = RESTRICT)
}

internal object ContactGroups : Table() {
    @JvmField val contactId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    @JvmField val groupId = integer("group_id").primaryKey().references(Groups.id, onDelete = RESTRICT, onUpdate = RESTRICT)
}
