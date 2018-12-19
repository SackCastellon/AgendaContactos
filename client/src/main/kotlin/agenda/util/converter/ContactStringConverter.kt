package agenda.util.converter

import agenda.model.Contact
import javafx.util.StringConverter

object ContactStringConverter : StringConverter<Contact>() {
    override fun toString(contact: Contact): String = contact.name
    override fun fromString(string: String): Contact = throw UnsupportedOperationException()
}
