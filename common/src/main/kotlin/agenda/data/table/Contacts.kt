package agenda.data.table

import agenda.model.Contact
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.ReferenceOption.RESTRICT
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Contacts : IdTable<Contact>() {
    val firstName: Column<String> = varchar("first_name", NAME_LENGTH)
    val lastName: Column<String> = varchar("last_name", NAME_LENGTH)

    override fun ResultRow.toData(): Contact = Contact(
        id = this[id],
        firstName = this[firstName],
        lastName = this[lastName],
        phones = TODO(),
        emails = TODO(),
        groups = TODO()
    )
}

object ContactPhones : Table() {
    val contactId: Column<Int> = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    val phoneId: Column<Int> = integer("phone_id").primaryKey().references(Phones.id, onDelete = CASCADE, onUpdate = RESTRICT)
}

object ContactEmails : Table() {
    val contactId: Column<Int> = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    val emailId: Column<Int> = integer("email_id").primaryKey().references(Emails.id, onDelete = CASCADE, onUpdate = RESTRICT)
}

object ContactGroups : Table() {
    val contactId: Column<Int> = integer("contact_id").primaryKey().references(Contacts.id, onDelete = CASCADE, onUpdate = RESTRICT)
    val groupId: Column<Int> = integer("group_id").primaryKey().references(Groups.id, onDelete = RESTRICT, onUpdate = RESTRICT)
}
