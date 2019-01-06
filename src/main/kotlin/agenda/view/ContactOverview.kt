package agenda.view

import agenda.data.dao.IDao
import agenda.model.Contact
import agenda.model.Email
import agenda.model.Group
import agenda.model.Phone
import agenda.util.ContactQuery
import agenda.util.selectFirst
import agenda.util.selectList
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.input.Clipboard
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import mu.KotlinLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.material.Material
import tornadofx.*
import java.awt.Desktop
import java.net.URI

class ContactOverview : Fragment(), KoinComponent {

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    private val searchBox: TextField by fxid()

    private val contactsTable: TableView<Contact> by fxid()
    val selectedContact: ReadOnlyObjectProperty<Contact> = contactsTable.selectionModel.selectedItemProperty()

    private val contactDetails: VBox by fxid()

    private val groups: Button by fxid()
    private val newContact: Button by fxid()
    private val editContact: Button by fxid()
    private val deleteContact: Button by fxid()

    init {
        contactsTable.apply {
            column(messages["overview.column.contacts"], Contact::fullNameProperty)
        }

        val data = SortedFilteredList(get<IDao<Contact>>("contacts").observable).bindTo(contactsTable)


        // Select first contact in list after filtering
        searchBox.textProperty().onChange {
            val query = ContactQuery.parse(it.orEmpty())
            data.predicate = query.predicate
            if (!query.isEmpty) runLater { contactsTable.selectFirst() }
        }

        // Reselect edited contact after edit finished
        selectedContact.addListener { _, oldValue, newValue ->
            if (oldValue != null && newValue == null) runLater { contactsTable.selectFirst { oldValue.id == it.id } }
        }


        groups.apply {
            action(::handleGroups)
        }

        newContact.apply {
            action { with(ContactEditor) { new() } }
        }

        editContact.apply {
            action { with(ContactEditor) { edit(selectedContact.value!!.id) } }
            enableWhen(selectedContact.isNotNull)
        }

        deleteContact.apply {
            action { with(ContactEditor) { delete(selectedContact.value!!.id) } }
            enableWhen(selectedContact.isNotNull)
        }

        contactDetails.apply {
            removeWhen(selectedContact.isNull)

            // Contact image
            imageview(selectedContact.select(Contact::imageProperty).objectBinding { it ?: defaultImage }) {
                fitHeight = 100.0
                fitWidth = 100.0
                isPreserveRatio = true
            }

            // Contact name
            label(selectedContact.select(Contact::fullNameProperty)).style { fontSize = 18.px }

            // Contact phones
            vbox(5) {
                val phones = selectedContact.selectList(Contact::phonesProperty)
                removeWhen { phones.booleanBinding { it.isNullOrEmpty() } }
                bindChildren(phones) {
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
                                    item(messages["context.copy"])
                                        .action { Clipboard.getSystemClipboard().setContent { putString(it.phone) } }
                                }
                            }
                        }
                        spacer()
                        button {
                            padding = insets(4)
                            graphic = FontIcon.of(Material.PHONE, 19)
                            action { browse("tel:${it.phone}") }
                        }
                        button {
                            padding = insets(4)
                            graphic = FontIcon.of(Material.SMS, 19)
                            action { browse("sms:${it.phone}") }
                        }
                    }
                }
            }

            // Contact emails
            vbox(5) {
                val emails = selectedContact.selectList(Contact::emailsProperty)
                removeWhen { emails.booleanBinding { it.isNullOrEmpty() } }
                bindChildren(emails) {
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
                                    item(messages["context.copy"]) {
                                        action { Clipboard.getSystemClipboard().putString(it.email) }
                                    }
                                }
                            }
                        }
                        spacer()
                        button {
                            padding = insets(4)
                            graphic = FontIcon.of(Material.EMAIL, 19)
                            action { browse("mailto:${it.email}") }
                        }
                    }
                }
            }

            // Contact groups
            vbox(2) {
                val groups = selectedContact.selectList(Contact::groupsProperty)
                removeWhen { groups.booleanBinding { it.isNullOrEmpty() } }
                label(messages["overview.field.groups"]) {
                    style {
                        fontSize = 10.px
                        textFill = Color.GRAY
                    }
                }
                label(groups.stringBinding { it?.joinToString(transform = Group::name).orEmpty() })
            }
        }
    }

    private fun handleGroups() {
        find<GroupsViewer> { openModal(block = true, resizable = false) }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
        private val defaultImage by lazy { Image("/images/grey_silhouette.png") }
        private val SUPPORTS_BROWSE = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)

        @JvmStatic private fun browse(uri: String) {
            if (SUPPORTS_BROWSE) URI(uri).also { logger.debug { "Browsing URI: $it" } }.let { Desktop.getDesktop().browse(it) }
        }
    }
}
