package agenda.model

import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

sealed class EditableGroup(
    id: Int,
    name: String
) : Group(id, name) {
    val nameProperty: StringProperty = SimpleStringProperty(name)
    override var name: String by nameProperty

    companion object {
        internal val defaults = listOf("Familia", "Amigos", "Trabajo")

        fun empty(): EditableGroup = New()
        fun create(id: Int, name: String): EditableGroup = Existing(id, name)
    }

    private class New : EditableGroup(newId, ""), Data.New<Group>
    private class Existing(
        id: Int,
        name: String
    ) : EditableGroup(id, name), Data.Existing<Group> {
        init {
            Data.checkId(id)
        }
    }

    class ViewModel(group: EditableGroup = empty()) : ItemViewModel<EditableGroup>(group) {
        val name: Property<String> = bind(EditableGroup::nameProperty)
    }
}
