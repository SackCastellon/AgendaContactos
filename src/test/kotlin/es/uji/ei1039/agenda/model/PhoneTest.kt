package es.uji.ei1039.agenda.model

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
            org.junit.jupiter.api.Assertions.assertTrue(isNew)
        }

        with(phone) {
            org.junit.jupiter.api.Assertions.assertFalse(isNew)
        }
    }

    @Test
    fun `phone id`() {
        with(emptyPhone) {
            org.junit.jupiter.api.Assertions.assertEquals(-1, id)
        }

        with(phone) {
            org.junit.jupiter.api.Assertions.assertEquals(42, id)
        }
    }

    @Test
    fun `phone value`() {
        with(emptyPhone) {
            org.junit.jupiter.api.Assertions.assertEquals("", phone)
            phone = "+34915245862"
            org.junit.jupiter.api.Assertions.assertEquals("+34915245862", phone)
        }

        with(phone) {
            org.junit.jupiter.api.Assertions.assertEquals("+34600123123", phone)
            phone = "+34932942032"
            org.junit.jupiter.api.Assertions.assertEquals("+34932942032", phone)
        }
    }

    @Test
    fun `phone label`() {
        with(emptyPhone) {
            org.junit.jupiter.api.Assertions.assertNull(label)
            label = Phone.Label.WORK
            org.junit.jupiter.api.Assertions.assertEquals(Phone.Label.WORK, label)
        }

        with(phone) {
            org.junit.jupiter.api.Assertions.assertEquals(Phone.Label.MOBILE, label)
            label = Phone.Label.HOME
            org.junit.jupiter.api.Assertions.assertEquals(Phone.Label.HOME, label)
        }
    }
}
