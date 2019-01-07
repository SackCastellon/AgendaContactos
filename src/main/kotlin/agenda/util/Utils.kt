@file:JvmName("Utils")

package agenda.util

import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableListValue
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableListValue
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import mu.KotlinLogging
import java.awt.Desktop
import java.io.File
import java.net.URI

private val logger = KotlinLogging.logger {}

fun <T, N> ObservableValue<T>.selectList(nested: (T) -> ObservableListValue<N>): ListProperty<N> {
    fun extractNested() = value?.let(nested)

    var currentNested = extractNested()

    return object : SimpleListProperty<N>() {
        val changeListener = ChangeListener<Any?> { _, _, _ ->
            invalidated()
            fireValueChangedEvent()
        }

        init {
            currentNested?.addListener(changeListener)
            this@selectList.addListener(changeListener)
        }

        override fun invalidated() {
            currentNested?.removeListener(changeListener)
            currentNested = extractNested()
            currentNested?.addListener(changeListener)
        }

        override fun get() = currentNested?.value

        override fun set(v: ObservableList<N>?) {
            (currentNested as? WritableListValue<*>)?.value = v
            super.set(v)
        }
    }
}

fun <T> TableView<T>.selectFirst(scrollTo: Boolean = true, condition: (T) -> Boolean) {
    items.firstOrNull(condition)?.let {
        selectionModel.select(it)
        if (scrollTo) scrollTo(it)
    }
}

private val SUPPORTS_OPEN = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)
private val SUPPORTS_BROWSE = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)

fun open(file: File): Unit = file.takeIf { SUPPORTS_OPEN }?.also { logger.debug { "Browsing: $it" } }?.let { Desktop.getDesktop().open(it) } ?: Unit
fun browse(uri: URI): Unit = uri.takeIf { SUPPORTS_BROWSE }?.also { logger.debug { "Browsing: $it" } }?.let { Desktop.getDesktop().browse(it) } ?: Unit
