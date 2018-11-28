package es.uji.ei1039.agenda.view

import es.uji.ei1039.agenda.model.Contact
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tornadofx.Fragment
import tornadofx.selectedItem

class ContactOverview : Fragment() {

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    private val contactsTable: TableView<Contact> by fxid()

    private val newContact: Button by fxid()
    private val editContact: Button by fxid()
    private val deleteContact: Button by fxid()

    @FXML
    private fun handleNewContact(event: ActionEvent) {
        val contactEditor = find<ContactEditor>().also { it.openModal(escapeClosesWindow = false, resizable = false, block = true) }

        if (contactEditor.success) {
            val contact = contactEditor.contact
            // TODO Save contact
        }
    }

    @FXML
    private fun handleEditContact(event: ActionEvent) {
        val selectedContact = contactsTable.selectedItem

        if (selectedContact == null) {
            logger.error("There is no selected contact to be edited!")
            return
        }

        val contactEditor = find<ContactEditor>(ContactEditor::contact to selectedContact)
            .also { it.openModal(escapeClosesWindow = false, resizable = false, block = true) }

        if (contactEditor.success) {
            val contact = contactEditor.contact
            // TODO Save contact
        }

    }

    @FXML
    private fun handleDeleteContact(event: ActionEvent) {
        // TODO
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ContactEditor::class.java)
    }
}
