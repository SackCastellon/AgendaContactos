package es.uji.ei1039.agenda.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public final class ContactOverview {

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

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert newContact != null : "fx:id=\"newContact\" was not injected: check your FXML file 'ContactOverview.fxml'.";
        assert editContact != null : "fx:id=\"editContact\" was not injected: check your FXML file 'ContactOverview.fxml'.";
        assert deleteContact != null : "fx:id=\"deleteContact\" was not injected: check your FXML file 'ContactOverview.fxml'.";
    }

    @FXML
    void handleNewContact(final ActionEvent event) {
        // TODO
    }

    @FXML
    void handleEditContact(final ActionEvent event) {
        // TODO
    }

    @FXML
    void handleDeleteContact(final ActionEvent event) {
        // TODO
    }
}
