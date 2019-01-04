package agenda.util

import agenda.model.Contact
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ContactQueryTest {

    private lateinit var contacts: List<Contact>

    private fun ContactQuery.process(): List<Int> = contacts.filter(predicate).sortedWith(comparator).map(Contact::id)

    @BeforeEach
    fun setup() {
        contacts = listOf(
            Contact.create(1, "Luis", "Garcia", emptyList(), emptyList(), emptyList()),
            Contact.create(2, "Pedro", "Garcia", emptyList(), emptyList(), emptyList()),
            Contact.create(3, "Maria", "Garcia", emptyList(), emptyList(), emptyList()),
            Contact.create(4, "Maria", "Espinosa", emptyList(), emptyList(), emptyList()),
            Contact.create(5, "Maria", "Lopez", emptyList(), emptyList(), emptyList()),
            Contact.create(6, "Pepe", "Hernandez", emptyList(), emptyList(), emptyList()),
            Contact.create(7, "Garcia", "Lopez", emptyList(), emptyList(), emptyList())
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
}
