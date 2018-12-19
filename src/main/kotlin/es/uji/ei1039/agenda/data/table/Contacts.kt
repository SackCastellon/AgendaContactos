package es.uji.ei1039.agenda.data.table

import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT
import org.jetbrains.exposed.sql.Table

object Contacts : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val surname = varchar("surname", 100)
}

object ContactPhones : Table() {
    val contatId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = RESTRICT, onUpdate = RESTRICT)
    val phoneId = integer("phone_id").primaryKey().references(Phones.id, onDelete = RESTRICT, onUpdate = RESTRICT)
}

object ContactEmails : Table() {
    val contatId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = RESTRICT, onUpdate = RESTRICT)
    val emailId = integer("email_id").primaryKey().references(Emails.id, onDelete = RESTRICT, onUpdate = RESTRICT)
}

object ContactGroups : Table() {
    val contatId = integer("contact_id").primaryKey().references(Contacts.id, onDelete = RESTRICT, onUpdate = RESTRICT)
    val groupId = integer("group_id").primaryKey().references(Groups.id, onDelete = RESTRICT, onUpdate = RESTRICT)
}
