package agenda.model

import agenda.model.Email
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EmailTest {

    private lateinit var emptyEmail: Email
    private lateinit var email: Email

    @BeforeEach
    fun setup() {
        emptyEmail = Email.empty()
        email = Email.create(42, "info@example.com", Email.Label.WORK)
    }

    @Test
    fun `email is new`() {
        with(emptyEmail) {
            assertTrue(isNew)
        }

        with(email) {
            assertFalse(isNew)
        }
    }

    @Test
    fun `email id`() {
        with(emptyEmail) {
            assertEquals(-1, id)
        }

        with(email) {
            assertEquals(42, id)
        }
    }

    @Test
    fun `email value`() {
        with(emptyEmail) {
            assertEquals("", email)
            email = "example@example.com"
            assertEquals("example@example.com", email)
        }

        with(email) {
            assertEquals("info@example.com", email)
            email = "contact@example.com"
            assertEquals("contact@example.com", email)
        }
    }

    @Test
    fun `email label`() {
        with(emptyEmail) {
            assertNull(label)
            label = Email.Label.WORK
            assertEquals(Email.Label.WORK, label)
        }

        with(email) {
            assertEquals(Email.Label.WORK, label)
            label = Email.Label.PERSONAL
            assertEquals(Email.Label.PERSONAL, label)
        }
    }
}
