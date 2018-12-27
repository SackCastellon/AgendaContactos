package es.uji.ei1039.agenda.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GroupTest {

    private lateinit var emptyGroup: Group
    private lateinit var group: Group

    @BeforeEach
    fun setup() {
        emptyGroup = Group.empty()
        group = Group.create(42, "Test")
    }

    @Test
    fun `group is new`() {
        with(emptyGroup) {
            assertTrue(isNew)
        }

        with(group) {
            assertFalse(isNew)
        }
    }

    @Test
    fun `group id`() {
        with(emptyGroup) {
            assertEquals(-1, id)
        }

        with(group) {
            assertEquals(42, id)
        }
    }

    @Test
    fun `group name`() {
        with(emptyGroup) {
            assertEquals("", name)
            name = "Hello"
            assertEquals("Hello", name)
        }

        with(group) {
            assertEquals("Test", name)
            name = "World"
            assertEquals("World", name)
        }
    }
}
