package agenda.util

import agenda.model.Contact
import agenda.model.Group
import agenda.model.Phone
import agenda.util.ContactQuery.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ContactQueryTest {

    private lateinit var contacts: List<Contact>

    private fun ContactQuery.process(): List<Int> = contacts.filter(predicate).sortedWith(comparator).map(Contact::id)

    @BeforeEach
    fun setup() {
        contacts = listOf(
            Contact.create(1, "Luis", "Garcia").also {
                it.phones.addAll(Phone.create(1, "600123456", Phone.Label.HOME))
            },
            Contact.create(2, "Pedro", "Garcia"),
            Contact.create(3, "Maria", "Garcia").also {
                it.phones.addAll(Phone.create(2, "964263581", Phone.Label.HOME))
                it.groups.addAll(Group.create(1, "Familia"))
            },
            Contact.create(4, "Maria", "Espinosa"),
            Contact.create(5, "Maria", "Lopez"),
            Contact.create(6, "Pepe", "Hernandez"),
            Contact.create(7, "Garcia", "Lopez").also {
                it.phones.addAll(Phone.create(3, "915586459", Phone.Label.HOME))
                it.groups.addAll(Group.create(1, "Familia"))
            }
        )
    }

    @Test
    fun `empty query shows all contacts`() {
        val result = ContactQuery.parse("").process()
        assertIterableEquals(listOf(1, 2, 3, 4, 5, 6, 7), result)
    }

    @Test
    fun `blank query shows all contacts`() {
        val result = ContactQuery.parse("    ").process()
        assertIterableEquals(listOf(1, 2, 3, 4, 5, 6, 7), result)
    }

    @Test
    fun `single word query`() {
        val result = ContactQuery.parse("Garcia").process()
        assertIterableEquals(listOf(1, 2, 3, 7), result)
    }

    @Test
    fun `multi word query`() {
        val result = ContactQuery.parse("Maria Espinosa").process()
        assertIterableEquals(listOf(4, 3, 5), result)
    }

    @Test
    fun `multi word query with multiple whitespaces`() {
        val result = ContactQuery.parse(" Maria   Espinosa    ").process()
        assertIterableEquals(listOf(4, 3, 5), result)
    }

    @Test
    fun `quoted multi word query`() {
        val result = ContactQuery.parse("\"Maria Garcia\"").process()
        assertIterableEquals(listOf(3), result)
    }

    @Test
    fun `quoted and unquoted multi word query`() {
        val result = ContactQuery.parse("\"Maria Garcia\" Lopez").process()
        assertIterableEquals(listOf(3, 5, 7), result)
    }

    @Test
    fun `quoted and unquoted multi word query with multiple whitespaces`() {
        val result = ContactQuery.parse("   \"Maria   Garcia\"        Lopez    ").process()
        assertIterableEquals(listOf(5, 7), result)
    }

    @Test
    fun `complex query with first name filter`() {
        val result = ContactQuery.parse("firstName:Garcia").process()
        assertIterableEquals(listOf(7), result)
    }

    @Test
    fun `complex query with last name filter and multiple words`() {
        val result = ContactQuery.parse("Pedro lastName:Garcia").process()
        assertIterableEquals(listOf(2, 1, 3), result)
    }

    @Test
    fun `complex query with phones and groups`() {
        val result = ContactQuery.parse("phones:Casa groups:Familia").process()
        assertIterableEquals(listOf(3, 7), result)
    }

    @Test fun `empty query`() {
        val query = ContactQuery.parse("")

        assert(query is EmptyQuery)
    }

    @Test fun `blank query`() {
        val query = ContactQuery.parse("   \t    \n    ")

        assert(query is EmptyQuery)
    }

    @Test fun `simple query`() {
        val query = ContactQuery.parse("Lorem ipsum \"dolor sit\" amet \"consectetur adipiscing elit\"")

        assert(query is SimpleQuery)
        assertIterableEquals(listOf("Lorem", "ipsum", "dolor sit", "amet", "consectetur adipiscing elit"), (query as SimpleQuery).words)
    }

    @Test fun `complex query`() {
        val query = ContactQuery.parse("Duis eget firstName:est non lacus lastName: faucibus groups:egestas \"in eget\" veli test:imperdiet")

        assert(query is ComplexQuery)

        query as ComplexQuery

        assertIterableEquals(listOf("Duis", "eget"), query.words)
        assertEquals(3, query.fields.size)
        assertIterableEquals(listOf("est", "non", "lacus"), query.fields["firstName"])
        assertIterableEquals(listOf("faucibus"), query.fields["lastName"])
        assertIterableEquals(listOf("egestas", "in eget", "veli"), query.fields["groups"])
    }
}
