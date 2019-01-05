package agenda.view

import agenda.data.dao.IDao
import agenda.model.Contact
import agenda.model.Email
import agenda.model.Group
import agenda.model.Phone
import agenda.util.ContactQuery
import agenda.util.selectList
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
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
    private val searchLabel: Label by fxid()

    private val contactsTable: TableView<Contact> by fxid()
    val selectedContact: ReadOnlyObjectProperty<Contact> = contactsTable.selectionModel.selectedItemProperty()

    private val contactDetails: VBox by fxid()

    private val groups: Button by fxid()
    private val newContact: Button by fxid()
    private val editContact: Button by fxid()
    private val deleteContact: Button by fxid()



    init {
        contactsTable.apply {
            column(messages["column.contacts"], Contact::fullNameProperty)
        }

        val data = SortedFilteredList(get<IDao<Contact>>("contacts").observable).bindTo(contactsTable)

        searchBox.textProperty().onChange { data.predicate = ContactQuery.parse(it.orEmpty()).predicate }

        searchLabel.apply {
            text=messages["search.label"]
        }

        groups.apply {
            action(::handleGroups)
            text=messages["button.groups"]

        }

        newContact.apply {
            action { with(ContactEditor) { new() } }
            text=messages["button.new"]
        }

        editContact.apply {
            action { with(ContactEditor) { edit(selectedContact.value!!) } }
            enableWhen(selectedContact.isNotNull)
            text=messages["button.edit"]
        }

        deleteContact.apply {
            action { with(ContactEditor) { delete(selectedContact.value!!) } }
            enableWhen(selectedContact.isNotNull)
            text=messages["button.delete"]
        }

        contactDetails.apply {
            removeWhen(selectedContact.isNull)

            // Contact image
            imageview(selectedContact.select(Contact::imageProperty).objectBinding { it ?: DEFAULT_IMAGE }) {
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
                label(messages["field.groups"]) {
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
        private val DEFAULT_IMAGE by lazy { Image("/images/grey_silhouette.png") }
        private val SUPPORTS_BROWSE by lazy { Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) }

        private fun browse(uri: String): Unit = if (SUPPORTS_BROWSE) Desktop.getDesktop().browse(URI(uri)) else Unit
    }
}
