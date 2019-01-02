package agenda.view

import agenda.model.Contact
import agenda.model.Email
import agenda.model.Group
import agenda.model.Phone
import agenda.view.styles.EditorStyles
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javafx.geometry.Pos
import javafx.scene.control.ButtonBar
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import org.apache.commons.validator.routines.EmailValidator
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.material.Material
import tornadofx.*
import tornadofx.ValidationTrigger.OnChange

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
        top {
            hbox {
                addClass(EditorStyles.header)
                vbox(5) {
                    label(messages["title"])
                    label(messages["heading.${mode.name.toLowerCase()}"]) {
                        addClass(EditorStyles.heading)
                    }
                }
            }
        }
        center {
            scrollpane(fitToWidth = true) {
                vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
                prefHeight = 350.0

                vbox(7) {
                    addClass(EditorStyles.content)
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
                        vbox(5).bindChildren(model.phones) {
                            hbox(5) {
                                lateinit var validatorPhone: ValidationContext.Validator<String>
                                lateinit var validatorLabel: ValidationContext.Validator<Phone.Label>
                                textfield(it.phoneProperty) {
                                    hgrow = Priority.ALWAYS
                                    prefWidth = 100.0
                                    promptText = messages["field.phone"]
                                    validatorPhone = requireValidPhone()
                                }
                                choicebox(it.labelProperty, Phone.Label.values().asList()) {
                                    converter = Phone.Label.converter
                                    prefWidth = 85.0
                                    validatorLabel = required()
                                }
                                button {
                                    padding = insets(4)
                                    graphic = FontIcon.of(Material.REMOVE, 18)
                                    action {
                                        model.validationContext.validators.removeAll(validatorPhone, validatorLabel)
                                        model.phones.apply {
                                            val phone = value.first(it::equals)
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
                        vbox(5).bindChildren(model.emails) {
                            hbox(5) {
                                lateinit var validatorEmail: ValidationContext.Validator<String>
                                lateinit var validatorLabel: ValidationContext.Validator<Email.Label>
                                textfield(it.emailProperty) {
                                    hgrow = Priority.ALWAYS
                                    prefWidth = 100.0
                                    promptText = messages["field.email"]
                                    validatorEmail = requireValidEmail()
                                }
                                choicebox(it.labelProperty, Email.Label.values().asList()) {
                                    converter = Email.Label.converter
                                    prefWidth = 85.0
                                    validatorLabel = required()
                                }
                                button {
                                    padding = insets(4)
                                    graphic = FontIcon.of(Material.REMOVE, 18)
                                    action {
                                        model.validationContext.validators.removeAll(validatorEmail, validatorLabel)
                                        model.emails.apply {
                                            val email = value.first(it::equals)
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
                addClass(EditorStyles.buttons)
                button(messages["button.cancel"], ButtonBar.ButtonData.CANCEL_CLOSE) {
                    isCancelButton = true
                    action { close() }
                }
                button(messages["button.save"], ButtonBar.ButtonData.OK_DONE) {
                    isDefaultButton = true
                    action {
                        model.commit {
                            _success = true
                            close()
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

    private fun TextField.requireValidPhone(): ValidationContext.Validator<String> {
        return model.validationContext.addValidator(this, textProperty(), OnChange()) {
            when {
                it.isNullOrBlank() -> error(messages["error.field.blank"])
                !phoneUtil.runCatching { isValidNumber(parse(it, "ES")) }.getOrDefault(false) -> error(messages["error.field.invalidPhone"])
                else -> null
            }
        }
    }

    private fun TextField.requireValidEmail(): ValidationContext.Validator<String> {
        return model.validationContext.addValidator(this, textProperty(), OnChange()) {
            when {
                it.isNullOrBlank() -> error(messages["error.field.blank"])
                !emailValidator.isValid(it) -> error(messages["error.field.invalidEmail"])
                else -> null
            }
        }
    }

    private inline fun <reified T : Any> ChoiceBox<T>.required(): ValidationContext.Validator<T> {
        return model.validationContext.addValidator(this, valueProperty(), OnChange()) {
            when (it) {
                null -> error(messages["error.field.empty"])
                else -> null
            }
        }
    }

    private fun openGroupSelector() {
        find<GroupSelector> {
            groupsList.bindContentBidirectional(model.groups)
            openModal(block = true, resizable = false)
            groupsList.unbindContentBidirectional(model.groups)
        }
    }

    enum class Mode { CREATE, EDIT }
}
