package es.uji.ei1039.agenda.view

import es.uji.ei1039.agenda.data.dao.IDao
import es.uji.ei1039.agenda.model.Contact
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.input.Clipboard
import javafx.scene.input.KeyCode.C
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination.CONTROL_DOWN
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import mu.KotlinLogging
import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.material.Material
import tornadofx.*
import java.awt.Desktop
import java.net.URI

class ContactOverview : Fragment() {

    private val contacts: IDao<Contact> by di("contacts")

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    private val searchBox: TextField by fxid()

    private val contactsTable: TableView<Contact> by fxid()

    private val contactDetails: VBox by fxid()

    private val newContact: Button by fxid()
    private val editContact: Button by fxid()
    private val deleteContact: Button by fxid()

    init {
        contactsTable.items.setAll(contacts.getAll())//FIXME
        contactsTable.apply {
            column<Contact, String>(messages["column.name"]) { it.value.fullnameProperty }
        }

        editContact.enableWhen { contactsTable.selectionModel.selectedItemProperty().isNotNull }
        deleteContact.enableWhen { contactsTable.selectionModel.selectedItemProperty().isNotNull }

        contactDetails.apply {
            val contact = contactsTable.selectionModel.selectedItemProperty()

            visibleWhen(contact.isNotNull)

            // Contact Image
            imageview(contact.select(Contact::imageProperty).objectBinding { it ?: DEFAULT_IMAGE }) {
                fitHeight = 100.0
                fitWidth = 100.0
                isPreserveRatio = true
            }

            // Contact name
            label(contact.select(Contact::fullnameProperty)).style { fontSize = 18.px }

            // Contact phones
            vbox(7.0) {
                contact.select(Contact::phonesProperty).onChange {
                    children.clear()
                    it?.forEach {
                        hbox(7.0) {
                            vbox(4.0) {
                                label(it.labelProperty/*, converter = TODO()*/) {
                                    // TODO Add translation support
                                    style {
                                        fontSize = 10.px
                                        textFill = Color.GRAY
                                    }
                                }
                                label(it.phoneProperty) {
                                    contextmenu {
                                        item(messages["context.copy"], KeyCodeCombination(C, CONTROL_DOWN))
                                            .action { Clipboard.getSystemClipboard().setContent { putString(it.phone) } }
                                    }
                                }
                            }
                            spacer()
                            button {
                                prefHeight = 27.0
                                prefWidth = 27.0
                                graphic = FontIcon.of(Material.PHONE, 20)
                                action { browse("tel:${it.phone}") }
                            }
                            button {
                                prefHeight = 27.0
                                prefWidth = 27.0
                                graphic = FontIcon.of(Material.SMS, 20)
                                action { browse("sms:${it.phone}") }
                            }
                        }
                    }
                }
            }

            // Contact emails
            vbox(7.0) {
                contact.select(Contact::emailsProperty).onChange {
                    children.clear()
                    it?.forEach {
                        hbox(7.0) {
                            vbox(4.0) {
                                label(it.labelProperty/*, converter = TODO()*/) {
                                    // TODO Add translation support
                                    style {
                                        fontSize = 10.px
                                        textFill = Color.GRAY
                                    }
                                }
                                label(it.emailProperty) {
                                    contextmenu {
                                        item(messages["context.copy"], KeyCodeCombination(C, CONTROL_DOWN)) {
                                            action { Clipboard.getSystemClipboard().putString(it.email) }
                                        }
                                    }
                                }
                            }
                            spacer()
                            button {
                                prefHeight = 27.0
                                prefWidth = 27.0
                                graphic = FontIcon.of(Material.EMAIL, 20)
                                action { browse("mailto:${it.email}") }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun browse(uri: String) {
        if (SUPPORTS_BROWSE) Desktop.getDesktop().browse(URI(uri))
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
        private val DEFAULT_IMAGE by lazy { Image("/images/grey_silhouette.png") }
        private val SUPPORTS_BROWSE = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
    }
}
