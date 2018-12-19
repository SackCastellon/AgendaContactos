package agenda.model

import agenda.model.IEmail.Label
import javafx.beans.property.*
import tornadofx.ItemViewModel
import tornadofx.getValue

class Email : IEmail {
    val emailProperty: StringProperty = SimpleStringProperty()
    override val email: String by emailProperty

    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty()
    override val label: Label by labelProperty

    class ViewModel(email: Email) : ItemViewModel<Email>(email) {
        val email: Property<String> = bind(Email::emailProperty)
        val label: Property<Label> = bind(Email::labelProperty)
    }
}
