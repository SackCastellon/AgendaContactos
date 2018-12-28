package agenda.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PhoneTest {

    private lateinit var emptyPhone: Phone
    private lateinit var phone: Phone

    @BeforeEach
    fun setup() {
        emptyPhone = Phone.empty()
        phone = Phone.create(42, "+34600123123", Phone.Label.MOBILE)
    }

    @Test
    fun `phone is new`() {
        with(emptyPhone) {
            assertTrue(isNew)
        }

        with(phone) {
            assertFalse(isNew)
        }
    }

    @Test
    fun `phone id`() {
        with(emptyPhone) {
            assertEquals(-1, id)
        }

        with(phone) {
            assertEquals(42, id)
        }
    }

    @Test
    fun `phone value`() {
        with(emptyPhone) {
            assertEquals("", phone)
            phone = "+34915245862"
            assertEquals("+34915245862", phone)
        }

        with(phone) {
            assertEquals("+34600123123", phone)
            phone = "+34932942032"
            assertEquals("+34932942032", phone)
        }
    }

    @Test
    fun `phone label`() {
        with(emptyPhone) {
            assertNull(label)
            label = Phone.Label.WORK
            assertEquals(Phone.Label.WORK, label)
        }

        with(phone) {
            assertEquals(Phone.Label.MOBILE, label)
            label = Phone.Label.HOME
            assertEquals(Phone.Label.HOME, label)
        }
    }
}
