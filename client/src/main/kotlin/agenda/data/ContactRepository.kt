package agenda.data

import agenda.model.Contact
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class ContactRepository : IRepository<Contact> {
    private val contacts: ObservableList<Contact> = FXCollections.observableArrayList()

    init {
        // TODO Remove hardcoded test data
        add(Contact().apply {
            name = "Pepe"
            surname = "García"
        })
        add(Contact().apply {
            name = "Luís"
            surname = "Lopez"
        })
    }

    override fun getAll(): ObservableList<Contact> = FXCollections.unmodifiableObservableList(contacts)

    override fun getById(id: Int): Contact? = TODO()

    override fun add(item: Contact) {
        contacts.add(item)
    }

    override fun remove(id: Int): Unit = TODO()

    override fun remove(item: Contact) {
        contacts.remove(item)
    }
}
