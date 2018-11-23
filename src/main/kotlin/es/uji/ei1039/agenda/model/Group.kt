package es.uji.ei1039.agenda.model

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.getValue
import tornadofx.setValue

class Group {
    val nameProperty: StringProperty = SimpleStringProperty()
    var name: String by nameProperty
}
