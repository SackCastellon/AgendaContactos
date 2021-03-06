package agenda.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.binding.Bindings
import javafx.beans.binding.StringExpression
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.image.Image
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

sealed class Contact(
    @get:JsonIgnore
    override val id: Int,
    firstName: String = "",
    lastName: String = "",
    phones: List<Phone> = emptyList(),
    emails: List<Email> = emptyList(),
    groups: List<Group> = emptyList()
) : IData {
    @Suppress("LeakingThis")
    @get:JsonIgnore
    override val isNew: Boolean = this is New

    @get:JsonIgnore
    val imageProperty: ObjectProperty<Image?> = SimpleObjectProperty() // TODO
    @get:JsonIgnore
    var image: Image? by imageProperty

    @get:JsonIgnore
    val firstNameProperty: StringProperty = SimpleStringProperty(firstName)
    var firstName: String by firstNameProperty

    @get:JsonIgnore
    val lastNameProperty: StringProperty = SimpleStringProperty(lastName)
    var lastName: String by lastNameProperty

    @get:JsonIgnore
    val fullNameProperty: StringExpression = Bindings.format("%s %s", firstNameProperty, lastNameProperty)
    @get:JsonIgnore
    val fullName: String by fullNameProperty

    @get:JsonIgnore
    val phonesProperty: ListProperty<Phone> = SimpleListProperty(FXCollections.observableArrayList(phones))
    var phones: ObservableList<Phone> by phonesProperty

    @get:JsonIgnore
    val emailsProperty: ListProperty<Email> = SimpleListProperty(FXCollections.observableArrayList(emails))
    var emails: ObservableList<Email> by emailsProperty

    @get:JsonIgnore
    val groupsProperty: ListProperty<Group> = SimpleListProperty(FXCollections.observableArrayList(groups))
    var groups: ObservableList<Group> by groupsProperty

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contact) return false

        if (id != other.id) return false
        if (image != other.image) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phones != other.phones) return false
        if (emails != other.emails) return false
        if (groups != other.groups) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + image.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phones.hashCode()
        result = 31 * result + emails.hashCode()
        result = 31 * result + groups.hashCode()
        return result
    }

    override fun toString(): String {
        return "Contact(id=$id, firstName=$firstName, lastName=$lastName, phones=$phones, emails=$emails, groups=$groups)"
    }

    companion object {
        @JvmStatic fun empty(): Contact = New()
        @JvmStatic fun create(
            id: Int,
            firstName: String,
            lastName: String//,
//            phones: List<Phone>,
//            emails: List<Email>,
//            groups: List<Group>
        ): Contact = Existing(id, firstName, lastName/*, phones, emails, groups*/)
    }

    private class New : Contact(-1)
    private class Existing(
        id: Int,
        firstName: String,
        lastName: String/*,
        phones: List<Phone>,
        emails: List<Email>,
        groups: List<Group>*/
    ) : Contact(id, firstName, lastName/*, phones, emails, groups*/)

    class ViewModel(contact: Contact) : ItemViewModel<Contact>(contact) {
        val firstName: Property<String> = bind(Contact::firstNameProperty)
        val lastName: Property<String> = bind(Contact::lastNameProperty)
        val phones: ListProperty<Phone> = bind(Contact::phonesProperty)
        val emails: ListProperty<Email> = bind(Contact::emailsProperty)
        val groups: ListProperty<Group> = bind(Contact::groupsProperty)
    }
}
