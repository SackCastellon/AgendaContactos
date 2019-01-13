package agenda.model

open class Contact(
    override val id: Int = -1,
    open val firstName: String,
    open val lastName: String,
    open val phones: List<Phone>,
    open val emails: List<Email>,
    open val groups: List<Group>
) : Data<Contact> {
    open val image: ByteArray = byteArrayOf()
    open val fullName: String get() = "$firstName $lastName"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contact) return false

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phones != other.phones) return false
        if (emails != other.emails) return false
        if (groups != other.groups) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phones.hashCode()
        result = 31 * result + emails.hashCode()
        result = 31 * result + groups.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "Contact(id=$id, firstName='$firstName', lastName='$lastName', phones=$phones, emails=$emails, groups=$groups, image=${image.contentToString()})"
    }
}
