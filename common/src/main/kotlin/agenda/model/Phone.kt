package agenda.model

open class Phone(
    override val id: Int,
    open val phone: String,
    open val label: Label
) : Data<Phone> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Phone) return false

        if (id != other.id) return false
        if (phone != other.phone) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + phone.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }

    override fun toString(): String {
        return "Phone(id=$id, phone='$phone', label=$label)"
    }

    enum class Label {
        HOME, WORK, MOBILE;

        companion object
    }
}
