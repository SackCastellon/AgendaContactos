package agenda.util

import agenda.model.Contact
import agenda.util.ContactQuery.Companion.FieldState.NAME
import agenda.util.ContactQuery.Companion.FieldState.VALUE
import agenda.util.ContactQuery.Companion.State.*
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.*

sealed class ContactQuery {
    abstract val predicate: (Contact) -> Boolean
    abstract val comparator: Comparator<in Contact>

    private object EmptyQuery : ContactQuery() {
        override val predicate: (Contact) -> Boolean = { true }
        override val comparator: Comparator<in Contact> = compareBy(Contact::id)
    }

    private class SimpleQuery(words: List<String>) : ContactQuery() {
        private val cache = Caffeine.newBuilder()
            .build<Contact, Int> { contact ->
                val fieldValues =
                    listOf(contact.fullName) +
                            contact.phones.flatMap { listOf(it.label.name, it.phone) } +
                            contact.emails.flatMap { listOf(it.label.name, it.email) } +
                            contact.groups.map { it.name }

                words.sumBy { w -> fieldValues.count { it.contains(w) } }
            }

        override val predicate: (Contact) -> Boolean = { cache[it]!! > 0 }
        override val comparator: Comparator<in Contact> = compareBy({ -cache[it]!! }, Contact::id)
    }

    private class ComplexQuery(words: List<String>, fields: Map<String, String>) : ContactQuery() {
        companion object {
            val fieldMap = mapOf(
                "firstName" to Contact::firstName,
                "lastName" to Contact::lastName,
                "fullName" to Contact::fullName
            )
        }

        private val cache = Caffeine.newBuilder()
            .build<Contact, Int> { contact ->
                val count = fields.count { (name, value) -> fieldMap.getValue(name).invoke(contact).contains(value) }
                if (count == 0) 0
                else {
                    val fieldValues =
                        listOf(contact.fullName) +
                                contact.phones.flatMap { listOf(it.label.name, it.phone) } +
                                contact.emails.flatMap { listOf(it.label.name, it.email) } +
                                contact.groups.map { it.name }

                    words.sumBy { w -> fieldValues.count { it.contains(w) } } + count
                }
            }

        override val predicate: (Contact) -> Boolean = { cache[it]!! > 0 }
        override val comparator: Comparator<in Contact> = compareBy({ -cache[it]!! }, Contact::id)
    }

    companion object {
        fun parse(query: String): ContactQuery {
            if (query.isBlank()) return EmptyQuery

            val words = mutableListOf<String>()
            val fields = mutableMapOf<String, String>()

            val buffer = mutableListOf<Char>()

            var currentField = ""

            var state = State.NONE
            var fieldState = FieldState.NONE

            val trim = query.trim()
            trim.forEachIndexed { i, c ->
                val prevState = state
                when {
                    c == ' ' -> if (prevState != QUOTED_LITERAL) state = NONE
                    c == '"' -> {
                        state = when (prevState) {
                            NONE, LITERAL -> QUOTED_LITERAL
                            QUOTED_LITERAL -> NONE
                        }
                    }
                    c == ':' -> if (prevState == LITERAL) fieldState = NAME
                    c.isLetterOrDigit() -> {
                        if (prevState == NONE) state = LITERAL
                        else if (fieldState == NAME) fieldState = VALUE
                    }
                }

                if ((state == LITERAL || (state == prevState && prevState == QUOTED_LITERAL)) && fieldState != NAME) buffer += c

                if (state == LITERAL && fieldState == NAME) {
                    currentField = String(buffer.toCharArray())
                    buffer.clear()
                }

                if (prevState == LITERAL && (state != LITERAL || i == trim.lastIndex) && fieldState == VALUE) {
                    if (currentField in ComplexQuery.fieldMap.keys)
                        fields[currentField] = String(buffer.toCharArray())
                    currentField = ""
                    buffer.clear()
                }

                if ((state == NONE && (prevState == LITERAL || prevState == QUOTED_LITERAL)) || i == trim.lastIndex) {
                    if (buffer.isNotEmpty()) words.add(String(buffer.toCharArray()))
                    buffer.clear()
                }
            }

            return if (fields.isEmpty()) SimpleQuery(words) else ComplexQuery(words, fields)
        }

        private enum class State { NONE, LITERAL, QUOTED_LITERAL }
        private enum class FieldState { NONE, NAME, VALUE }
    }
}
