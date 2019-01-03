package agenda.data.dao

import javafx.beans.binding.ListBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.observable

abstract class AbstractDao<T> : IDao<T> {
    private val listBinding: ListBinding<T> by lazy {
        object : ListBinding<T>() {
            override fun computeValue(): ObservableList<T> = getAll().observable()
        }
    }

    final override val observable: ObservableList<T> get() = FXCollections.unmodifiableObservableList(listBinding)

    final override fun invalidate(): Unit = listBinding.invalidate()
}
