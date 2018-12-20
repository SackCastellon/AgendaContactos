package es.uji.ei1039.agenda.model

import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

sealed class Contact(
    val id: Int,
    name: String = "",
    surname: String = "",
    phones: List<Phone> = emptyList(),
    emails: List<Email> = emptyList(),
    groups: List<Group> = emptyList()
) {
    val nameProperty: StringProperty = SimpleStringProperty(name)
    var name: String by nameProperty

    val surnameProperty: StringProperty = SimpleStringProperty(surname)
    var surname: String by surnameProperty

    val phonesProperty: ListProperty<Phone> = SimpleListProperty(FXCollections.observableArrayList(phones))
    var phones: ObservableList<Phone> by phonesProperty

    val emailsProperty: ListProperty<Email> = SimpleListProperty(FXCollections.observableArrayList(emails))
    var emails: ObservableList<Email> by emailsProperty

    val groupsProperty: ListProperty<Group> = SimpleListProperty(FXCollections.observableArrayList(groups))
    var groups: ObservableList<Group> by groupsProperty

    class New : Contact(-1)
    class Existing(
        id: Int,
        name: String,
        surname: String,
        phones: List<Phone>,
        emails: List<Email>,
        groups: List<Group>
    ) : Contact(id, name, surname, phones, emails, groups)

    class ViewModel(contact: Contact) : ItemViewModel<Contact>(contact) {
        val name: Property<String> = bind(Contact::nameProperty)
        val surname: Property<String> = bind(Contact::surnameProperty)
        val phones: ObservableList<Phone> = bind(Contact::phonesProperty)
        val emails: ObservableList<Email> = bind(Contact::emailsProperty)
        val groups: ObservableList<Group> = bind(Contact::groupsProperty)
    }
}
