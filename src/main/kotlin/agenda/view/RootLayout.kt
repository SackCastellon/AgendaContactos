package agenda.view

import agenda.data.dao.IDao
import agenda.model.Contact
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.ButtonType
import javafx.scene.input.KeyCode.*
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination.CONTROL_DOWN
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import tornadofx.*
import tornadofx.controlsfx.statusbar

class RootLayout : View(), KoinComponent {

    private val selectedContact = SimpleObjectProperty<Contact>()

    override val root = borderpane {
        prefHeight = 500.0
        prefWidth = 500.0

        top {
            menubar {
                menu(messages["menu.file"]) {
                    item(messages["menu.file.export"]) {
                        val contacts = get<IDao<Contact>>("contacts").observable
                        disableWhen(booleanBinding(contacts, op = List<*>::isEmpty))
                        action { find<ContactExporter> { openModal(resizable = false) } }
                    }
                    separator()
                    item(messages["menu.file.quit"]) {
                        action {
                            confirmation(
                                messages["confirmation.quitApp"],
                                buttons = *arrayOf(ButtonType.YES, ButtonType.NO)
                            ) { if (it == ButtonType.YES) Platform.exit() }
                        }
                    }
                }
                menu(messages["menu.edit"]) {
                    item(messages["menu.edit.new"], KeyCodeCombination(N, CONTROL_DOWN)) {
                        action { with(ContactEditor) { new() } }
                    }
                    item(messages["menu.edit.edit"], KeyCodeCombination(E, CONTROL_DOWN)) {
                        disableWhen(selectedContact.isNull)
                        action { with(ContactEditor) { edit(selectedContact.value!!.id) } }
                    }
                    item(messages["menu.edit.delete"], KeyCodeCombination(DELETE)) {
                        disableWhen(selectedContact.isNull)
                        action { with(ContactEditor) { delete(selectedContact.value!!.id) } }
                    }
                }
                menu(messages["menu.help"]) {
                    item(messages["menu.help.manual"]) {
                        action { TODO("Open manual") }
                    }
                    item(messages["menu.help.about"]) {
                        action { TODO("Open about dialog") }
                    }
                }
            }
        }
        center = find<ContactOverview> { this@RootLayout.selectedContact.bind(selectedContact) }.root
        bottom {
            statusbar()
        }
    }

    init {
        title = messages["title"]

        runLater {
            currentStage?.apply {
                minWidth = 425.0
                minHeight = 300.0
            }

            primaryStage.setOnCloseRequest { event ->
                confirmation(
                    messages["confirmation.quitApp"],
                    buttons = *arrayOf(ButtonType.YES, ButtonType.NO)
                ) { if (it == ButtonType.NO) event.consume() }
            }
        }
    }
}
