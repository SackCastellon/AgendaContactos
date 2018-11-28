package es.uji.ei1039.agenda.model

import javafx.beans.property.*
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class Phone {
    val phoneProperty: StringProperty = SimpleStringProperty()
    var phone: String by phoneProperty

    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty()
    var label: Label by labelProperty

    enum class Label {
        HOME,
        WORK,
        MOBILE
    }

    class ViewModel(phone: Phone) : ItemViewModel<Phone>(phone) {
        val phone: Property<String> = bind(Phone::phoneProperty)
        val label: Property<Label> = bind(Phone::labelProperty)
    }
}
