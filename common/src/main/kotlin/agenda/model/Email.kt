package agenda.model

open class Email(
    override val id: Int,
    open val email: String,
    open val label: Label
) : Data<Email> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Email) return false

        if (id != other.id) return false
        if (email != other.email) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + email.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }

    override fun toString(): String {
        return "Email(id=$id, email='$email', label=$label)"
    }

    enum class Label {
        PERSONAL, WORK;

        companion object
    }
}
