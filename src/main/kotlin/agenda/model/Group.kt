package agenda.model

import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

sealed class Group(
    val id: Int,
    name: String = ""
) {
    val isNew: Boolean by lazy { this is New }

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
        fun empty(): Group = New()
        fun create(id: Int, name: String): Group = Existing(id, name)
    }

    private class New : Group(-1)
    private class Existing(
        id: Int,
        name: String
    ) : Group(id, name)

    class ViewModel(group: Group) : ItemViewModel<Group>(group) {
        val name: Property<String> = bind(Group::nameProperty)
    }
}
