package agenda.model

import agenda.util.StringConverter
import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.*
import javafx.util.StringConverter
import tornadofx.*

sealed class Email(
    @get:JsonIgnore
    override val id: Int,
    email: String = "",
    label: Label? = null
) : IData {
    @Suppress("LeakingThis")
    @get:JsonIgnore
    override val isNew: Boolean = this is New

    @get:JsonIgnore
    val emailProperty: StringProperty = SimpleStringProperty(email)
    var email: String by emailProperty

    @get:JsonIgnore
    val labelProperty: ObjectProperty<Label> = SimpleObjectProperty(label)
    var label: Label by labelProperty

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Email) return false

        if (id != other.id) return false
        if (email != other.email) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + email.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }

    override fun toString(): String {
        return "Email(id=$id, email=$email, label=$label)"
    }

    companion object {
        @JvmStatic fun empty(): Email = New()
        @JvmStatic fun empty(idBlacklist: Set<Int>): Email = New((-1 downTo Int.MIN_VALUE).first { it !in idBlacklist })
        @JvmStatic fun create(id: Int, email: String, label: Label): Email = Existing(id, email, label)
    }

    private class New(id: Int = -1) : Email(id) {
        init {
            require(id < 0) { "Id of new email must be negative" }
        }
    }

    private class Existing(
        id: Int,
        email: String,
        label: Label
    ) : Email(id, email, label) {
        init {
            require(id >= 0) { "Id of existing email must be positive" }
        }
    }

    enum class Label {
        PERSONAL,
        WORK;

        companion object {
            @JvmField val converter: StringConverter<Label> = StringConverter { FX.messages["contact.email.label.${it.name.toLowerCase()}"] }
        }
    }

    class ViewModel(email: Email) : ItemViewModel<Email>(email) {
        val email: Property<String> = bind(Email::emailProperty)
        val label: Property<Label> = bind(Email::labelProperty)
    }
}
