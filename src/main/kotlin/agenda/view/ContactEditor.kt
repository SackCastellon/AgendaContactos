package agenda.view

import agenda.data.dao.IDao
import agenda.model.Contact
import agenda.model.Email
import agenda.model.Group
import agenda.model.Phone
import agenda.view.styles.CommonStyles
import agenda.view.styles.EditorStyles
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import org.apache.commons.validator.routines.EmailValidator
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.material.Material
import tornadofx.*
import javafx.scene.control.Alert.AlertType


class ContactEditor : Fragment() {

    private val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()
    private val emailValidator: EmailValidator = EmailValidator.getInstance()

    /** The contact to be edited. If no contact is passed, then a empty contact is created. */
    val contact: Contact by param(Contact.empty())

    private val model = Contact.ViewModel(contact)

    /** The mode in which the editor is open */
    private val mode = if (contact.isNew) Mode.CREATE else Mode.EDIT

    private var _success = false
    /** Whether the contact was edited successfully (save button is pressed). */
    val success: Boolean get() = _success

    override val root: BorderPane = borderpane {
        addClass(EditorStyles.editor)

        val phones = FXCollections.observableArrayList<Phone.ViewModel>().apply { bind(model.phones, Phone::ViewModel) }
        val emails = FXCollections.observableArrayList<Email.ViewModel>().apply { bind(model.emails, Email::ViewModel) }

        top {
            hbox {
                addClass(CommonStyles.header)
                vbox(5) {
                    label(messages["title"])
                    label(messages["heading.${mode.name.toLowerCase()}"]) {
                        addClass(CommonStyles.heading)
                    }
                }
            }
        }
        center {
            scrollpane(fitToWidth = true) {
                vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
                prefHeight = 350.0

                vbox(7) {
                    addClass(CommonStyles.content)
                    prefWidth = 300.0

                    // TODO Contact image

                    // Contact name
                    vbox(5) {
                        label(messages["field.name"]).addClass(EditorStyles.fieldset)
                        textfield(model.firstName) {
                            promptText = messages["field.name.first"]
                            required(message = messages["error.field.blank"])
                        }
                        textfield(model.lastName) {
                            promptText = messages["field.name.last"]
                        }
                    }

                    // Contact phones
                    vbox(5) {
                        hbox(5, Pos.BOTTOM_LEFT) {
                            label(messages["field.phones"]).addClass(EditorStyles.fieldset)
                            spacer()
                            button {
                                padding = insets(4)
                                graphic = FontIcon.of(Material.ADD, 18)
                                action {
                                    model.phones.apply {
                                        val idSet = value.filter { it.isNew }.map { it.id }.toSet()
                                        value = (value + Phone.empty(idSet)).observable() // TODO Find a better solution
                                    }
                                }
                            }
                        }
                        vbox(5).bindChildren(phones) {
                            hbox(5) {
                                textfield(it.phone) {
                                    hgrow = Priority.ALWAYS
                                    prefWidth = 100.0
                                    promptText = messages["field.phone"]
                                    requireValidPhone()
                                }
                                choicebox(it.label, Phone.Label.values().asList()) {
                                    converter = Phone.Label.converter
                                    prefWidth = 85.0
                                    required()
                                }
                                button {
                                    padding = insets(4)
                                    graphic = FontIcon.of(Material.REMOVE, 18)
                                    action {
                                        model.phones.apply {
                                            val phone = value.first(it.item::equals)
                                            value = (value - phone).observable() // TODO Find a better solution
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Contact emails
                    vbox(5) {
                        hbox(5, Pos.BOTTOM_LEFT) {
                            label(messages["field.emails"]).addClass(EditorStyles.fieldset)
                            spacer()
                            button {
                                padding = insets(4)
                                graphic = FontIcon.of(Material.ADD, 18)
                                action {
                                    model.emails.apply {
                                        val idSet = value.filter { it.isNew }.map { it.id }.toSet()
                                        value = (value + Email.empty(idSet)).observable() // TODO Find a better solution
                                    }
                                }
                            }
                        }
                        vbox(5).bindChildren(emails) {
                            hbox(5) {
                                textfield(it.email) {
                                    hgrow = Priority.ALWAYS
                                    prefWidth = 100.0
                                    promptText = messages["field.email"]
                                    requireValidEmail()
                                }
                                choicebox(it.label, Email.Label.values().asList()) {
                                    converter = Email.Label.converter
                                    prefWidth = 85.0
                                    required()
                                }
                                button {
                                    padding = insets(4)
                                    graphic = FontIcon.of(Material.REMOVE, 18)
                                    action {
                                        model.emails.apply {
                                            val email = value.first(it.item::equals)
                                            value = (value - email).observable() // TODO Find a better solution
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Contact groups
                    vbox(5) {
                        label(messages["field.groups"]).addClass(EditorStyles.fieldset)
                        textfield(model.groups.stringBinding { it?.joinToString(transform = Group::name).orEmpty() }) {
                            isEditable = false
                            setOnKeyPressed { if (it.code == KeyCode.SPACE || it.code == KeyCode.ENTER) openGroupSelector() }
                            setOnMouseClicked { openGroupSelector() }
                        }
                    }
                }
            }
        }
        bottom {
            buttonbar {
                addClass(CommonStyles.buttons)
                button(messages["button.cancel"], ButtonBar.ButtonData.CANCEL_CLOSE) {
                    isCancelButton = true
                    action(::close)
                }
                button(messages["button.save"], ButtonBar.ButtonData.OK_DONE) {
                    isDefaultButton = true
                    action {
                        var isValid = true
                        isValid = phones.fold(isValid) { valid, model -> model.validate(valid) && valid }
                        isValid = emails.fold(isValid) { valid, model -> model.validate(valid) && valid }
                        isValid = isValid and model.validate()
                        if (isValid) {
                            phones.forEach { it.commit() }
                            emails.forEach { it.commit() }
                            model.commit {
                                _success = true
                                close()
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        title = messages["title"]

        runLater {
            currentStage?.apply {
                minWidth = 225.0
                minHeight = 350.0
            }
        }
    }

    private fun TextField.requireValidPhone() = validator {
        when {
            it.isNullOrBlank() -> error(messages["error.field.blank"])
            !phoneUtil.runCatching { isValidNumber(parse(it, "ES")) }.getOrDefault(false) -> error(messages["error.field.invalidPhone"])
            else -> null
        }
    }

    private fun TextField.requireValidEmail() = validator {
        when {
            it.isNullOrBlank() -> error(messages["error.field.blank"])
            !emailValidator.isValid(it) -> error(messages["error.field.invalidEmail"])
            else -> null
        }
    }

    private inline fun <reified T : Any> ChoiceBox<T>.required() = validator {
        when (it) {
            null -> error(messages["error.field.empty"])
            else -> null
        }
    }

    private fun openGroupSelector() {
        find<GroupSelector> {
            groupsList.bindContentBidirectional(model.groups)
            openModal(block = true, resizable = false)
            groupsList.unbindContentBidirectional(model.groups)
        }
    }

    companion object : KoinComponent {
        private val contacts: IDao<Contact> by inject("contacts")

        @JvmStatic internal fun Component.new() {
            val editor = find<ContactEditor> { openModal(block = true) }
            if (editor.success) contacts.add(editor.contact)
        }

        @JvmStatic internal fun Component.edit(contact: Contact) {
            val editor = find<ContactEditor>(ContactEditor::contact to contact) { openModal(block = true) }
            if (editor.success) contacts.add(editor.contact)
        }

        @JvmStatic internal fun Component.delete(contact: Contact) {
            confirmation(
                messages["contact.deleteWarning.title"], messages["contact.deleteWarning.message"],
                buttons = *arrayOf(ButtonType.YES, ButtonType.NO)
            ) { if (it == ButtonType.YES) contacts.remove(contact.id) }
        }

    }

    enum class Mode { CREATE, EDIT }
}
