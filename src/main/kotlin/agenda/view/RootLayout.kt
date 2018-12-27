package agenda.view

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.get
import tornadofx.runLater

class RootLayout : View() {

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    init {
        title = messages["title"]

        root.center<ContactOverview>()

        runLater {
            currentStage?.apply {
                minWidth = 350.0
                minHeight = 300.0
            }
        }
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
