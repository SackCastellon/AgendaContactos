package agenda.model

import javafx.beans.property.*
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class Contact : IContact {
    val nameProperty: StringProperty = SimpleStringProperty()
    override var name: String by nameProperty

    val surnameProperty: StringProperty = SimpleStringProperty()
    override var surname: String by surnameProperty

    val phonesProperty: ListProperty<Phone> = SimpleListProperty()
    override val phones: ObservableList<Phone> by phonesProperty

    val emailsProperty: ListProperty<Email> = SimpleListProperty()
    override val emails: ObservableList<Email> by emailsProperty

    val groupsProperty: ListProperty<Group> = SimpleListProperty()
    override val groups: ObservableList<Group> by groupsProperty

    class ViewModel(contact: Contact) : ItemViewModel<Contact>(contact) {
        val name: Property<String> = bind(Contact::nameProperty)
        val surname: Property<String> = bind(Contact::surnameProperty)
        val phones: ObservableList<Phone> = bind(Contact::phonesProperty)
        val emails: ObservableList<Email> = bind(Contact::emailsProperty)
        val groups: ObservableList<Group> = bind(Contact::groupsProperty)
    }
}
