package es.uji.ei1039.agenda.model

import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList
import tornadofx.getValue
import tornadofx.setValue

class Contact {
    val nameProperty: StringProperty = SimpleStringProperty()
    var name: String by nameProperty

    val surnameProperty: StringProperty = SimpleStringProperty()
    var surname: String by surnameProperty

    val phonesProperty: ListProperty<Phone> = SimpleListProperty()
    var phones: ObservableList<Phone> by phonesProperty

    val emailsProperty: ListProperty<Email> = SimpleListProperty()
    var emails: ObservableList<Email> by emailsProperty

    val groupsProperty: ListProperty<Group> = SimpleListProperty()
    var groups: ObservableList<Group> by groupsProperty
}
