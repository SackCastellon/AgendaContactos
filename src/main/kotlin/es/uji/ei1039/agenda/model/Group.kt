package es.uji.ei1039.agenda.model

import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class Group {
    val nameProperty: StringProperty = SimpleStringProperty()
    var name: String by nameProperty

    class ViewModel(group: Group) : ItemViewModel<Group>(group) {
        val name: Property<String> = bind(Group::nameProperty)
    }
}
