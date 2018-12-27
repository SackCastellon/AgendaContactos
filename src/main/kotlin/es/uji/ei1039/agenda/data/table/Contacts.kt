package es.uji.ei1039.agenda.data.table

import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT
import org.jetbrains.exposed.sql.Table

internal object Contacts : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val firstName = varchar("first_name", 100)
    val lastName = varchar("last_name", 100)
}

internal object ContactPhones : Table() {
    val contactId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    val phoneId = integer("phone_id").primaryKey().references(Phones.id, onDelete = RESTRICT, onUpdate = RESTRICT)
}

internal object ContactEmails : Table() {
    val contactId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    val emailId = integer("email_id").primaryKey().references(Emails.id, onDelete = RESTRICT, onUpdate = RESTRICT)
}

internal object ContactGroups : Table() {
    val contactId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    val groupId = integer("group_id").primaryKey().references(Groups.id, onDelete = RESTRICT, onUpdate = RESTRICT)
}
