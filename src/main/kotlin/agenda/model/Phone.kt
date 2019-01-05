package agenda.model

import agenda.model.converter.StringConverter
import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.*
import javafx.util.StringConverter
import tornadofx.*

sealed class Phone(
    @get:JsonIgnore
    val id: Int,
    phone: String = "",
    label: Label? = null
) {
    @Suppress("LeakingThis")
    @get:JsonIgnore
    val isNew: Boolean = this is New

    @get:JsonIgnore
    val phoneProperty: StringProperty = SimpleStringProperty(phone)
    var phone: String by phoneProperty

    @get:JsonIgnore
    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty(label)
    var label: Label by labelProperty

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Phone) return false

        if (id != other.id) return false
        if (phone != other.phone) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + phone.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }

    override fun toString(): String {
        return "Phone(id=$id, phone=$phone, label=$label)"
    }

    companion object {
        @JvmStatic fun empty(): Phone = New()
        @JvmStatic fun empty(idBlacklist: Set<Int>): Phone = New((-1 downTo Int.MIN_VALUE).first { it !in idBlacklist })
        @JvmStatic fun create(id: Int, phone: String, label: Label): Phone = Existing(id, phone, label)
    }

    private class New(id: Int = -1) : Phone(id) {
        init {
            require(id < 0) { "Id of new phone must be negative" }
        }
    }

    private class Existing(
        id: Int,
        phone: String,
        label: Label
    ) : Phone(id, phone, label) {
        init {
            require(id >= 0) { "Id of existing phone must be positive" }
        }
    }

    enum class Label {
        HOME,
        WORK,
        MOBILE;

        companion object {
            @JvmField val converter: StringConverter<Label> = StringConverter { FX.messages["phone.label.${it.name.toLowerCase()}"] }
        }
    }

    class ViewModel(phone: Phone) : ItemViewModel<Phone>(phone) {
        val phone: Property<String> = bind(Phone::phoneProperty)
        val label: Property<Label> = bind(Phone::labelProperty)
    }
}
