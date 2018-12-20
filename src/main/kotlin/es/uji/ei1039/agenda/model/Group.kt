package es.uji.ei1039.agenda.model

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
    val nameProperty: StringProperty = SimpleStringProperty(name)
    var name: String by nameProperty

    class New : Group(-1)
    class Existing(
        id: Int,
        name: String
    ) : Group(id, name)

    class ViewModel(group: Group) : ItemViewModel<Group>(group) {
        val name: Property<String> = bind(Group::nameProperty)
    }
}
