package agenda.util

import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableListValue
import javafx.collections.ObservableList

fun <T, N> ObservableValue<T>.select(nested: (T) -> ListProperty<N>): ListProperty<N> {
    fun extractNested(): ListProperty<N>? = value?.let(nested)

    var currentNested: ListProperty<N>? = extractNested()

    return object : SimpleListProperty<N>() {
        val changeListener = ChangeListener<Any?> { _, _, _ ->
            invalidated()
            fireValueChangedEvent()
        }

        init {
            currentNested?.addListener(changeListener)
            this@select.addListener(changeListener)
        }

        override fun invalidated() {
            currentNested?.removeListener(changeListener)
            currentNested = extractNested()
            currentNested?.addListener(changeListener)
        }

        override fun get() = currentNested?.value

        override fun set(v: ObservableList<N>?) {
            (currentNested as? WritableListValue<N>)?.value = v
            super.set(v)
        }
    }
}
