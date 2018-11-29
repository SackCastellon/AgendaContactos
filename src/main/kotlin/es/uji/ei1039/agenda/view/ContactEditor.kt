package es.uji.ei1039.agenda.view

import es.uji.ei1039.agenda.model.Contact
import es.uji.ei1039.agenda.view.ContactEditor.Mode.CREATE
import es.uji.ei1039.agenda.view.ContactEditor.Mode.EDIT
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.get

class ContactEditor : View() {

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    private val tf_nom: TextField by fxid()
    private val tf_ap: TextField by fxid()

    /** The contact to be edited. If no contact is passed, then a new contact is created. */
    val contact: Contact by param(Contact())
    /** The mode in which the editor is open */
    val mode: Mode = if (params.containsKey(ContactEditor::contact.name)) EDIT else CREATE

    /** Whether the contact was edited successfully (save button is pressed). */
    var success: Boolean = false // TODO Set to true if the contact is edited successfully
        private set

    init {
        title = messages["title"]
    }

    enum class Mode { CREATE, EDIT }
}
