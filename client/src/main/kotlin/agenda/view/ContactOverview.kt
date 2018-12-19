package agenda.view

import agenda.data.IRepository
import agenda.model.Contact
import agenda.util.converter.ContactStringConverter
import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import org.controlsfx.control.textfield.AutoCompletionBinding
import org.controlsfx.control.textfield.TextFields
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tornadofx.*

class ContactOverview : Fragment() {

    private val contacts: IRepository<Contact> by di("contacts")

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    private val contactsTable: TableView<Contact> by fxid()
    private val detailsPane: GridPane by fxid()

    private val newContact: Button by fxid()
    private val editContact: Button by fxid()
    private val deleteContact: Button by fxid()
    private val searchBox: TextField by fxid()

    private val provider: ObjectProperty<IRepository<Contact>> = SimpleObjectProperty()

    init {
        val completionBinding: AutoCompletionBinding<Contact> = TextFields.bindAutoCompletion(searchBox, this::getSuggestions, ContactStringConverter)
        completionBinding.minWidthProperty().bind(searchBox.minWidthProperty())
        completionBinding.setOnAutoCompleted { event -> search(event.completion) }
        contactsTable.items = contacts.getAll()
        contactsTable.apply {
            column<Contact, String>(messages["column.name"]) { with(it.value) { Bindings.format("%s %s", nameProperty, surnameProperty) } }
        }

        editContact.enableWhen { contactsTable.selectionModel.selectedItemProperty().isNotNull }
        deleteContact.enableWhen { contactsTable.selectionModel.selectedItemProperty().isNotNull }
    }

    private fun search(contact: Contact) {


    }

    private fun getSuggestions(suggestionRequest: AutoCompletionBinding.ISuggestionRequest): List<Contact> {
        TODO() //provider.get().getSuggested(suggestionRequest.userText)
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

        contacts.remove(selectedContact)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ContactEditor::class.java)
    }
}
