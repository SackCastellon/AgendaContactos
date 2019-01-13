package agenda.model

import agenda.model.Data.Companion.checkId
import javafx.beans.binding.Bindings
import javafx.beans.binding.StringExpression
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

sealed class EditableContact(
    id: Int,
    firstName: String,
    lastName: String,
    phones: List<Phone>,
    emails: List<Email>,
    groups: List<Group>
) : Contact(id, firstName, lastName, phones, emails, groups) {
    // TODO Image

    val firstNameProperty: StringProperty = SimpleStringProperty(firstName)
    override var firstName: String by firstNameProperty

    val lastNameProperty: StringProperty = SimpleStringProperty(lastName)
    override var lastName: String by lastNameProperty

    val fullNameProperty: StringExpression = Bindings.format("%s %s", firstNameProperty, lastNameProperty)
    override val fullName: String by fullNameProperty

    val phonesProperty: ListProperty<Phone> = SimpleListProperty(FXCollections.observableArrayList(phones))
    override var phones: ObservableList<Phone> by phonesProperty

    val emailsProperty: ListProperty<Email> = SimpleListProperty(FXCollections.observableArrayList(emails))
    override var emails: ObservableList<Email> by emailsProperty

    val groupsProperty: ListProperty<Group> = SimpleListProperty(FXCollections.observableArrayList(groups))
    override var groups: ObservableList<Group> by groupsProperty

    companion object {
        fun empty(): EditableContact = New()
        fun create(
            id: Int,
            firstName: String,
            lastName: String,
            phones: List<Phone>,
            emails: List<Email>,
            groups: List<EditableGroup>
        ): EditableContact = Existing(id, firstName, lastName, phones, emails, groups)
    }

    private class New : EditableContact(newId, "", "", emptyList(), emptyList(), emptyList()), Data.New<Contact>
    private class Existing(
        id: Int,
        firstName: String,
        lastName: String,
        phones: List<Phone>,
        emails: List<Email>,
        groups: List<Group>
    ) : EditableContact(id, firstName, lastName, phones, emails, groups), Data.Existing<Contact> {
        init {
            checkId(id)
        }
    }

    class ViewModel(contact: EditableContact = empty()) : ItemViewModel<EditableContact>(contact) {
        val firstName: Property<String> = bind(EditableContact::firstNameProperty)
        val lastName: Property<String> = bind(EditableContact::lastNameProperty)
        val phones: ListProperty<Phone> = bind(EditableContact::phonesProperty)
        val emails: ListProperty<Email> = bind(EditableContact::emailsProperty)
        val groups: ListProperty<Group> = bind(EditableContact::groupsProperty)
    }
}
