package es.uji.ei1039.agenda.view

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.get

class RootLayout : View() {

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    init {
        title = messages["title"]

        root.center<ContactOverview>()
    }

    @FXML
    private fun handleNew(event: ActionEvent) {
        // TODO
    }

    @FXML
    private fun handleOpen(event: ActionEvent) {
        // TODO
    }

    @FXML
    private fun handleSave(event: ActionEvent) {
        // TODO
    }

    @FXML
    private fun handleQuit(event: ActionEvent) {
        Platform.exit()
    }
}
