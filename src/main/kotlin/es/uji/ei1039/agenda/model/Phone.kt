package es.uji.ei1039.agenda.model

import javafx.beans.property.*
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

sealed class Phone(
    val id: Int,
    phone: String = "",
    label: Label? = null
) {
    val phoneProperty: StringProperty = SimpleStringProperty(phone)
    var phone: String by phoneProperty

    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty(label)
    var label: Label by labelProperty

    class New : Phone(-1)
    class Existing(
        id: Int,
        phone: String,
        label: Label
    ) : Phone(id, phone, label)

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
