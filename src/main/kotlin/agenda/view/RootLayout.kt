package agenda.view

import agenda.model.Contact
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.ButtonType
import javafx.scene.control.Menu
import javafx.scene.input.KeyCode.*
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination.CONTROL_DOWN
import javafx.scene.layout.BorderPane
import tornadofx.*

class RootLayout : View() {

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    private val selectedContact = SimpleObjectProperty<Contact>()
    private val menuEdit: Menu by fxid()

    init {
        title = messages["title"]

        menuEdit.apply {
            item(messages["menu.edit.new"], KeyCodeCombination(N, CONTROL_DOWN)) {
                action { with(ContactEditor) { new() } }
            }
            item(messages["menu.edit.edit"], KeyCodeCombination(E, CONTROL_DOWN)) {
                disableWhen(selectedContact.isNull)
                action { with(ContactEditor) { edit(selectedContact.value!!) } }
            }
            item(messages["menu.edit.delete"], KeyCodeCombination(DELETE)) {
                disableWhen(selectedContact.isNull)
                action { with(ContactEditor) { delete(selectedContact.value!!) } }
            }
        }

        root.center = find<ContactOverview> { this@RootLayout.selectedContact.bind(selectedContact) }.root

        runLater {
            currentStage?.apply {
                minWidth = 425.0
                minHeight = 300.0
            }

            primaryStage.setOnCloseRequest { event ->
                confirmation(
                    messages["alert.close.header"],
                    buttons = *arrayOf(ButtonType.YES, ButtonType.NO)
                ) { if (it == ButtonType.NO) event.consume() }
            }
        }
    }

    @FXML
    private fun handleExport(event: ActionEvent) {
        find<ContactExporter> { openModal() }
    }

    @FXML
    private fun handleQuit(event: ActionEvent) {
        confirmation(
            messages["alert.close.header"],
            buttons = *arrayOf(ButtonType.YES, ButtonType.NO)
        ) { if (it == ButtonType.YES) Platform.exit() }
    }
}
