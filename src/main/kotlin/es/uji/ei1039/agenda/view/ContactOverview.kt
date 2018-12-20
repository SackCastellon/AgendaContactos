package es.uji.ei1039.agenda.view

import es.uji.ei1039.agenda.data.dao.IDao
import es.uji.ei1039.agenda.model.Contact
import es.uji.ei1039.agenda.util.converter.ContactStringConverter
import javafx.beans.binding.Bindings
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import mu.KotlinLogging
import org.controlsfx.control.textfield.AutoCompletionBinding
import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest
import org.controlsfx.control.textfield.TextFields
import tornadofx.*

class ContactOverview : Fragment() {

    private val contacts: IDao<Contact> by di("contacts")

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    private val contactsTable: TableView<Contact> by fxid()
    private val detailsPane: GridPane by fxid()

    private val newContact: Button by fxid()
    private val editContact: Button by fxid()
    private val deleteContact: Button by fxid()
    private val searchBox: TextField by fxid()

    private val fn_l: Label by fxid()
    private val ln_l: Label by fxid()
    private val ph_l: Label by fxid()
    private val gp_l: Label by fxid()
    private val em_l: Label by fxid()

    init {
        val completionBinding: AutoCompletionBinding<Contact> = TextFields.bindAutoCompletion(searchBox, this::getSuggestions, ContactStringConverter)
        completionBinding.minWidthProperty().bind(searchBox.minWidthProperty())
        completionBinding.setOnAutoCompleted { event -> search(event.completion) }
        contactsTable.items.setAll(contacts.getAll())//FIXME
        contactsTable.apply {
            column<Contact, String>(messages["column.name"]) { with(it.value) { Bindings.format("%s %s", nameProperty, surnameProperty) } }
        }

        editContact.enableWhen { contactsTable.selectionModel.selectedItemProperty().isNotNull }
        deleteContact.enableWhen { contactsTable.selectionModel.selectedItemProperty().isNotNull }
        showContactDetails(null)
        contactsTable.selectionModel.selectedItemProperty().addListener { _, _, newValue -> showContactDetails(newValue) }
    }

    private fun search(contact: Contact) {


    }

    private fun showContactDetails(person: Contact?) {
        if (person != null) {
            fn_l.text = person.name
            ln_l.text = person.surname
            ph_l.text = person.phonesProperty.toString()
            gp_l.text = person.groupsProperty.toString()
            em_l.text = person.emailsProperty.toString()

        } else {
            fn_l.text = ""
            ln_l.text = ""
            ph_l.text = ""
            gp_l.text = ""
            em_l.text = ""
        }
    }

    private fun getSuggestions(request: ISuggestionRequest): List<Contact> {
        val query = request.userText

        fun String.matches(): Boolean = contains(query, true)

        return contacts.getAll().asSequence().filter { contact ->
            contact.name.matches() ||
                    contact.surname.matches() ||
                    contact.phones.any { it.phone.matches() } ||
                    contact.emails.any { it.email.matches() } ||
                    contact.groups.any { it.name.matches() }
        }.toList()
    }


    @FXML
    private fun handleNewContact(event: ActionEvent) {
        val editor = find<ContactEditor>().also { it.openModal(escapeClosesWindow = false, resizable = false, block = true) }
        if (editor.success) contacts.add(editor.contact)
    }

    @FXML
    private fun handleEditContact(event: ActionEvent) {
        val selectedContact = contactsTable.selectedItem

        if (selectedContact == null) {
            logger.error("There is no selected contact to be edited!")
            return
        }

        val editor = find<ContactEditor>(ContactEditor::contact to selectedContact)
            .also { it.openModal(escapeClosesWindow = false, resizable = false, block = true) }

        if (editor.success) contacts.add(editor.contact)
    }

    @FXML
    private fun handleDeleteContact(event: ActionEvent) {
        val selectedContact = contactsTable.selectedItem

        if (selectedContact == null) {
            logger.error("There is no selected contact to be deleted!")
            return
        }

        contacts.remove(selectedContact.id)
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
