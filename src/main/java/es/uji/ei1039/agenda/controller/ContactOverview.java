package es.uji.ei1039.agenda.controller;

import es.uji.ei1039.agenda.App;
import es.uji.ei1039.agenda.model.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public final class ContactOverview {

    @FXML
    private TableView<Contact> contact_table;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="newContact"
    private Button newContact; // Value injected by FXMLLoader

    @FXML // fx:id="editContact"
    private Button editContact; // Value injected by FXMLLoader

    @FXML // fx:id="deleteContact"
    private Button deleteContact; // Value injected by FXMLLoader

    @FXML
    private App mainApp;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert newContact != null : "fx:id=\"newContact\" was not injected: check your FXML file 'ContactOverview.fxml'.";
        assert editContact != null : "fx:id=\"editContact\" was not injected: check your FXML file 'ContactOverview.fxml'.";
        assert deleteContact != null : "fx:id=\"deleteContact\" was not injected: check your FXML file 'ContactOverview.fxml'.";
    }

    @FXML
    void handleNewContact(final ActionEvent event) {
        Contact tempContact = new Contact();
        boolean onClicked = mainApp.showContactEditDialog(tempContact);
        if (onClicked){
            mainApp.getContactData().add(tempContact);
        }
    }

    @FXML
    void handleEditContact(final ActionEvent event) {
        /*Contact sel_con = contact_table.getSelectionModel().getSelectedItem();
        boolean onClicked = mainApp.showContactEditDialog(sel_con);
        if (onClicked){

        }
        */
    }

    @FXML
    void handleDeleteContact(final ActionEvent event) {
        // TODO
    }
}
