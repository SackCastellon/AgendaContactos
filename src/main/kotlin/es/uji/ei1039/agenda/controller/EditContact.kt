package es.uji.ei1039.agenda.controller


import es.uji.ei1039.agenda.model.Contact
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.text.Text
import javafx.stage.Stage




class EditContact {

    var contact: Contact? = null

    var dialogStage: Stage? = null

    @FXML
    private lateinit var title: Text

    @FXML
    private lateinit var nombre: TextField
}
