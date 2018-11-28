package es.uji.ei1039.agenda.model

import javafx.beans.property.*
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
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

    class ViewModel(contact: Contact) : ItemViewModel<Contact>(contact) {
        val name: Property<String> = bind(Contact::nameProperty)
        val surname: Property<String> = bind(Contact::surnameProperty)
        val phones: Property<ObservableList<Phone>> = bind(Contact::phonesProperty)
        val emails: Property<ObservableList<Email>> = bind(Contact::emailsProperty)
        val groups: Property<ObservableList<Group>> = bind(Contact::groupsProperty)
    }
}
