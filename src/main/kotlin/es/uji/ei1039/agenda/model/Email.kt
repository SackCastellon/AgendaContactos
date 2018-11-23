package es.uji.ei1039.agenda.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
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
}
