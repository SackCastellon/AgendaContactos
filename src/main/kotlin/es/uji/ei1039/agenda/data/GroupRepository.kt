package es.uji.ei1039.agenda.data

import es.uji.ei1039.agenda.model.Group
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class GroupRepository : IRepository<Group> {

    private val groups: ObservableList<Group> = FXCollections.observableArrayList()

    init {
        add(Group().apply { name = "Familia" })
        add(Group().apply { name = "Amigos" })
        add(Group().apply { name = "Trabajo" })
    }

    override fun getAll(): ObservableList<Group> = FXCollections.unmodifiableObservableList(groups)

    override fun getById(id: Int): Group? = TODO()

    override fun add(item: Group) {
        groups.add(item)
    }

    override fun remove(id: Int): Unit = TODO()

    override fun remove(item: Group) {
        groups.remove(item)
    }
}
