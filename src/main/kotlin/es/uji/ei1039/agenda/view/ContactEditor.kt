package es.uji.ei1039.agenda.view


import es.uji.ei1039.agenda.data.dao.IDao
import es.uji.ei1039.agenda.model.Contact
import es.uji.ei1039.agenda.model.Group
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import tornadofx.View
import tornadofx.get

class ContactEditor : View() {

    override val root: BorderPane by fxml(hasControllerAttribute = true)

    private val tf_nom: TextField by fxid()
    private val tf_ap: TextField by fxid()
    private val l_tit: Label by fxid()
    private val tel_box: VBox by fxid()
    private val em_box: VBox by fxid()
    private val gp_box: VBox by fxid()

    /** The contact to be edited. If no contact is passed, then a new contact is created. */
    val contact: Contact by param(Contact.New())
    /** The mode in which the editor is open */
    private val mode: Mode = if (contact is Contact.New) Mode.CREATE else Mode.EDIT

    private val groups: IDao<Group> by di("groups")

    /** Whether the contact was edited successfully (save button is pressed). */
    var success: Boolean = false // TODO Set to true if the contact is edited successfully
        private set

    init {
        title = messages["title"]
        l_tit.text = messages["title"]

        val gdesp = ChoiceBox<Group>()
        if (mode == Mode.EDIT) {
            tf_nom.text = contact.name
            tf_ap.text = contact.surname
            contact.phonesProperty.forEach { phone -> tel_box.children.add(TextField(phone.phone)) }

            contact.emailsProperty.forEach { email -> em_box.children.add(TextField(email.email)) }

            contact.groupsProperty.forEach { group -> val box = HBox(); box.add(Label(group.name));box.add(Button("X"));gp_box.children.add(box); }

            groups.getAll().forEach { group ->
                if (!contact.groupsProperty.get().contains(group)) {
                    gdesp.items.add(group)
                }
            }
            contact.groupsProperty.forEach { group -> val box = HBox(); box.add(Label(group.name));box.add(Button("X"));gp_box.children.add(box); }
            gp_box.add(ChoiceBox<Group>())

        } else{
            gdesp.items.addAll(groups.getAll())
        }
        tel_box.children.add(TextField())
        em_box.children.add(TextField())
        gp_box.add(gdesp)
    }

    enum class Mode { CREATE, EDIT }
}
