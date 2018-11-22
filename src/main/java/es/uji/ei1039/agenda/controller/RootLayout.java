package es.uji.ei1039.agenda.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public final class RootLayout {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    }

    @FXML
    void handleNew(final ActionEvent event) {
        // TODO
    }

    @FXML
    void handleOpen(final ActionEvent event) {
        // TODO
    }

    @FXML
    void handleSave(final ActionEvent event) {
        // TODO
    }

    @FXML
    void handleQuit(final ActionEvent event) {
        // TODO
    }
}
