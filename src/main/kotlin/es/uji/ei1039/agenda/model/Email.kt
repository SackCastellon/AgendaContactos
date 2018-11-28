package es.uji.ei1039.agenda.model

import javafx.beans.property.*
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class Email {
    val emailProperty: StringProperty = SimpleStringProperty()
    var email: String by emailProperty

    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty()
    var label: Label by labelProperty

    enum class Label {
        PERSONAL,
        WORK
    }

    class ViewModel(email: Email) : ItemViewModel<Email>(email) {
        val email: Property<String> = bind(Email::emailProperty)
        val label: Property<Label> = bind(Email::labelProperty)
    }
}
