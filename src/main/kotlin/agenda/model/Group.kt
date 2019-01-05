package agenda.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

sealed class Group(
    @get:JsonIgnore
    val id: Int,
    name: String = ""
) {
    @Suppress("LeakingThis")
    @get:JsonIgnore
    val isNew: Boolean = this is New

    @get:JsonIgnore
    val nameProperty: StringProperty = SimpleStringProperty(name)
    var name: String by nameProperty

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
        return "Group(id=$id, firstName=$name)"
    }

    companion object {
        internal val defaults = listOf("Familia", "Amigos", "Trabajo")

        internal fun default(name: String): Group = New().apply { this.name = name }
        fun empty(): Group = New()
        fun empty(idBlacklist: Set<Int>): Group = New((-1 downTo Int.MIN_VALUE).first { it !in idBlacklist })
        fun create(id: Int, name: String): Group = Existing(id, name)
    }

    private class New(id: Int = -1) : Group(id) {
        init {
            require(id < 0) { "Id of new phone must be negative" }
        }
    }

    private class Existing(
        id: Int,
        name: String
    ) : Group(id, name) {
        init {
            require(id >= 0) { "Id of existing phone must be positive" }
        }
    }

    class ViewModel(group: Group) : ItemViewModel<Group>(group) {
        val name: Property<String> = bind(Group::nameProperty)
    }
}

internal val Group.isDefault: Boolean get() = name in Group.defaults
