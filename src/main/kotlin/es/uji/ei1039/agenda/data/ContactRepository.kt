package es.uji.ei1039.agenda.data

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import es.uji.ei1039.agenda.model.Contact
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.util.*
import java.util.stream.Collectors


class ContactRepository : IRepository<Contact> {


    private val SUGGESTION_COUNT = 7L

    private val SUGGESTIONS: LoadingCache<String, List<Contact>> = Caffeine.newBuilder()
        .maximumSize(100_000L)
        .initialCapacity(1000)
        .build<String, List<Contact>> { query -> contacts
                .parallelStream()
                .filter { it -> !it.name.isEmpty() }
                .filter { it -> Math.abs(it.name.length - query.length) < 5 }
                .sorted(Comparator.comparing(Contact::name))
                .limit(SUGGESTION_COUNT)
                .collect(Collectors.toList<Contact>())
        }

    override fun getSuggested(userText : String): List<Contact> {
        if (userText.isEmpty()) return emptyList()
        return Objects.requireNonNull(SUGGESTIONS.get(userText.toLowerCase()))!!


    }

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
