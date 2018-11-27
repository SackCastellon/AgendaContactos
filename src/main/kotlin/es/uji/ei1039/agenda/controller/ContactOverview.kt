package es.uji.ei1039.agenda.controller

import es.uji.ei1039.agenda.App
import es.uji.ei1039.agenda.model.Contact
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableView
import java.net.URL
import java.util.*

class ContactOverview {

    @FXML
    private lateinit var contactsTable: TableView<Contact>

    @FXML
    private lateinit var resources: ResourceBundle

    @FXML
    private lateinit var location: URL

    @FXML
    private lateinit var newContact: Button

    @FXML
    private lateinit var editContact: Button

    @FXML
    private lateinit var deleteContact: Button

    private lateinit var mainApp: App

    @FXML
    private fun initialize() {
        // TODO
    }

    @FXML
    private fun handleNewContact(event: ActionEvent) {
        val tempContact = Contact()
        val onClicked = mainApp.showContactEditDialog(tempContact)
        if (onClicked) {
            //mainApp.getContactData().add(tempContact)
        }
    }

    @FXML
    private fun handleEditContact(event: ActionEvent) {
        // TODO
    }

    @FXML
    private fun handleDeleteContact(event: ActionEvent) {
        // TODO
    }

    fun setMainApp(mainApp : App){
        this.mainApp = mainApp
    }
}
