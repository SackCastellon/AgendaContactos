package agenda.model

import agenda.util.StringConverter
import javafx.beans.property.*
import tornadofx.*

sealed class EditablePhone(
    id: Int,
    phone: String,
    label: Label
) : Phone(id, phone, label) {

    val phoneProperty: StringProperty = SimpleStringProperty(phone)
    override var phone: String by phoneProperty

    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty(label)
    override var label: Label by labelProperty

    companion object {
        fun empty(): EditablePhone = New()
        fun create(
            id: Int,
            phone: String,
            label: Label
        ): EditablePhone = Existing(id, phone, label)
    }

    private class New : EditablePhone(newId, "", TODO()), Data.New<Phone>
    private class Existing(
        id: Int,
        phone: String,
        label: Label
    ) : EditablePhone(id, phone, label), Data.Existing<Phone> {
        init {
            Data.checkId(id)
        }
    }

    class ViewModel(phone: EditablePhone = empty()) : ItemViewModel<EditablePhone>(phone) {
        val phone: Property<String> = bind(EditablePhone::phoneProperty)
        val label: Property<Label> = bind(EditablePhone::labelProperty)
    }
}

private val labelConverter = StringConverter<Phone.Label> { FX.messages["contact.phone.label.${it.name.toLowerCase()}"] }

internal inline val Phone.Label.Companion.converter get() = labelConverter
