package agenda.view

import agenda.data.dao.IDao
import agenda.model.Contact
import agenda.model.Email
import agenda.model.Phone
import agenda.util.select
import javafx.geometry.Pos
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
        // TODO Implement search functionality

        contactsTable.items = contacts.observable
        contactsTable.apply {
            column(messages["column.name"], Contact::fullnameProperty)
        }

        val contact = contactsTable.selectionModel.selectedItemProperty()

        newContact.apply {
            action(::handleNewContact)
        }

        editContact.apply {
            action(::handleEditContact)
            enableWhen(contact.isNotNull)
        }

        deleteContact.apply {
            action(::handleDeleteContact)
            enableWhen(contact.isNotNull)
        }

        contactDetails.apply {
            visibleWhen(contact.isNotNull)

            // Contact image
            imageview(contact.select(Contact::imageProperty).objectBinding { it ?: DEFAULT_IMAGE }) {
                fitHeight = 100.0
                fitWidth = 100.0
                isPreserveRatio = true
            }

            // Contact name
            label(contact.select(Contact::fullnameProperty)).style { fontSize = 18.px }

            // Contact phones
            vbox(5).bindChildren(contact.select(Contact::phonesProperty)) {
                hbox(5, Pos.CENTER_LEFT) {
                    vbox(2) {
                        label(it.labelProperty, converter = Phone.Label.converter) {
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
                        padding = insets(4)
                        graphic = FontIcon.of(Material.PHONE, 20)
                        action { browse("tel:${it.phone}") }
                    }
                    button {
                        padding = insets(4)
                        graphic = FontIcon.of(Material.SMS, 20)
                        action { browse("sms:${it.phone}") }
                    }
                }
            }

            // Contact emails
            vbox(5).bindChildren(contact.select(Contact::emailsProperty)) {
                hbox(5, Pos.CENTER_LEFT) {
                    vbox(2) {
                        label(it.labelProperty, converter = Email.Label.converter) {
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
                        padding = insets(4)
                        graphic = FontIcon.of(Material.EMAIL, 20)
                        action { browse("mailto:${it.email}") }
                    }
                }
            }

            // TODO Contact groups
        }
    }

    private fun browse(uri: String): Unit = if (SUPPORTS_BROWSE) Desktop.getDesktop().browse(URI(uri)) else Unit

    private fun getSuggestions(request: ISuggestionRequest): List<Contact> {
        val query = request.userText

        fun String.matches(): Boolean = contains(query, true)

        return contacts.getAll().asSequence().filter { contact ->
            contact.firstName.matches() ||
                    contact.lastName.matches() ||
                    contact.phones.any { it.phone.matches() } ||
                    contact.emails.any { it.email.matches() } ||
                    contact.groups.any { it.name.matches() }
        }.toList()
    }

    private fun handleNewContact() {
        val editor = find<ContactEditor>().also {
            it.openModal(escapeClosesWindow = false, /*resizable = false,*/ block = true)
        }
        if (editor.success) contacts.add(editor.contact)
    }

    private fun handleEditContact() {
        val selectedContact = contactsTable.selectedItem

        if (selectedContact == null) {
            logger.error("There is no selected contact to be edited!")
            return
        }

        val editor = find<ContactEditor>(ContactEditor::contact to selectedContact).also {
            it.openModal(escapeClosesWindow = false, /*resizable = false,*/ block = true)
        }
        if (editor.success) contacts.add(editor.contact)
    }

    private fun handleDeleteContact() {
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
        private val SUPPORTS_BROWSE by lazy { Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) }
    }
}
