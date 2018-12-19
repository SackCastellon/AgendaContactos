package agenda.model

import agenda.model.IPhone.Label
import javafx.beans.property.*
import tornadofx.ItemViewModel
import tornadofx.getValue

class Phone : IPhone {
    val phoneProperty: StringProperty = SimpleStringProperty()
    override val phone: String by phoneProperty

    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty()
    override val label: Label by labelProperty

    class ViewModel(phone: Phone) : ItemViewModel<Phone>(phone) {
        val phone: Property<String> = bind(Phone::phoneProperty)
        val label: Property<Label> = bind(Phone::labelProperty)
    }
}
