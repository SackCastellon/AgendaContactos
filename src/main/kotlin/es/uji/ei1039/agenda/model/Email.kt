package es.uji.ei1039.agenda.model

import javafx.beans.property.*
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

sealed class Email(
    val id: Int,
    email: String = "",
    label: Label? = null
) {
    val emailProperty: StringProperty = SimpleStringProperty(email)
    var email: String by emailProperty

    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty(label)
    var label: Label by labelProperty

    class New : Email(-1)
    class Existing(
        id: Int,
        email: String,
        label: Label
    ) : Email(id, email, label)

    enum class Label {
        PERSONAL,
        WORK
    }

    class ViewModel(email: Email) : ItemViewModel<Email>(email) {
        val email: Property<String> = bind(Email::emailProperty)
        val label: Property<Label> = bind(Email::labelProperty)
    }
}
