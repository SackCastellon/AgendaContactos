package es.uji.ei1039.agenda

import es.uji.ei1039.agenda.controller.EditPersonController
import es.uji.ei1039.agenda.model.Contact
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.stage.Modality
import javafx.stage.Stage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

class App : Application() {

    private lateinit var primaryStage: Stage
    private lateinit var rootLayout: BorderPane

    override fun start(primaryStage: Stage) {
        log.debug("Starting application")

        this.primaryStage = primaryStage
        this.primaryStage.title = "Address Book"

        initRootLayout()
        showContactOverview()

        log.debug("Application started")
    }

    private fun initRootLayout() {
        log.debug("Loading Root Layout")
        try {
            val loader = FXMLLoader(javaClass.getResource("views/RootLayout.fxml"))
            rootLayout = loader.load()

            val scene = Scene(rootLayout)
            primaryStage.scene = scene
            primaryStage.show()
        } catch (e: IOException) {
            log.error("Error loading 'RootLayout.fxml'", e)
        }
    }

    private fun showContactOverview() {
        log.debug("Loading Contact Overview")
        try {
            val loader = FXMLLoader(javaClass.getResource("views/ContactOverview.fxml"))
            val contactOverview: BorderPane = loader.load()

            rootLayout.center = contactOverview
        } catch (e: IOException) {
            log.error("Error loading 'ContactOverview.fxml'", e)
        }
    }

    fun showContactEditDialog(contact: Contact): Boolean {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            val loader = FXMLLoader(javaClass.getResource("view/PersonEditDialog.fxml"))
            val page: AnchorPane = loader.load()

            // Create the dialog Stage.
            val dialogStage = Stage()
            dialogStage.title = "Edit Person"
            dialogStage.initModality(Modality.WINDOW_MODAL)
            dialogStage.initOwner(primaryStage)
            val scene = Scene(page)
            dialogStage.scene = scene

            // Set the person into the controller.
            val controller = loader.getController<EditPersonController>()
            //controller.setDialogStage(dialogStage);
            //controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait()

            return false //controller.isOkClicked();
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(App::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            log.debug("Launching application")
            Application.launch(App::class.java, *args)
            log.debug("Exiting application")
        }
    }
}
