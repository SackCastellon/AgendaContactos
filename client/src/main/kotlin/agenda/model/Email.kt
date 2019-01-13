package agenda.model

import agenda.model.Data.Companion.checkId
import agenda.util.StringConverter
import javafx.beans.property.*
import tornadofx.*

sealed class EditableEmail(
    id: Int,
    email: String,
    label: Label
) : Email(id, email, label) {
    val emailProperty: StringProperty = SimpleStringProperty(email)
    override var email: String by emailProperty

    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty(label)
    override var label: Label by labelProperty

    companion object {
        fun empty(): EditableEmail = New()
        fun create(id: Int, email: String, label: Label): EditableEmail = Existing(id, email, label)
    }

    private class New : EditableEmail(newId, "", TODO()), Data.New<Email>
    private class Existing(
        id: Int,
        email: String,
        label: Label
    ) : EditableEmail(id, email, label), Data.Existing<Email> {
        init {
            checkId(id)
        }
    }

    class ViewModel(email: EditableEmail = empty()) : ItemViewModel<EditableEmail>(email) {
        val email: Property<String> = bind(EditableEmail::emailProperty)
        val label: Property<Label> = bind(EditableEmail::labelProperty)
    }
}

private val labelConverter = StringConverter<Email.Label> { FX.messages["contact.email.label.${it.name.toLowerCase()}"] }

internal inline val Email.Label.Companion.converter get() = labelConverter
