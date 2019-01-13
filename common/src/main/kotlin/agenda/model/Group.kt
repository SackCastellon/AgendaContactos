package agenda.model

open class Group(
    override val id: Int,
    open val name: String
) : Data<Group> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Group) return false

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "Group(id=$id, name='$name')"
    }

    companion object
}
