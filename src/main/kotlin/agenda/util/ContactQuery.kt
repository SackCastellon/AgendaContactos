package agenda.util

import agenda.model.Contact
import agenda.model.IData
import agenda.util.ContactQuery.Companion.FieldState.NAME
import agenda.util.ContactQuery.Companion.FieldState.VALUE
import agenda.util.ContactQuery.Companion.State.*
import com.github.benmanes.caffeine.cache.Caffeine
import mu.KotlinLogging
import java.util.*

sealed class ContactQuery {
    abstract val predicate: (Contact) -> Boolean
    abstract val comparator: Comparator<in Contact>

    @Suppress("LeakingThis")
    val isEmpty: Boolean = this is EmptyQuery

    private object EmptyQuery : ContactQuery() {
        override val predicate: (Contact) -> Boolean = { true }
        override val comparator: Comparator<in IData> = compareBy(IData::id)
        override fun toString(): String = this::class.simpleName ?: super.toString()
    }

    private data class SimpleQuery(private val words: Set<String>) : ContactQuery() {
        private val cache = Caffeine.newBuilder()
            .build<Contact, Int> { contact ->
                val fieldValues =
                    listOf(contact.fullName) +
                            contact.phones.flatMap { listOf(it.label.name, it.phone) } +
                            contact.emails.flatMap { listOf(it.label.name, it.email) } +
                            contact.groups.map { it.name }

                words.sumBy { w -> fieldValues.count { it.contains(w, true) } }
            }

        override val predicate: (Contact) -> Boolean = { cache[it]!! > 0 }
        override val comparator: Comparator<in Contact> = compareBy({ -cache[it]!! }, Contact::id)
    }

    private data class ComplexQuery(private val words: Set<String>, private val fields: Map<String, Set<String>>) : ContactQuery() {
        companion object {
            @JvmField internal val fieldMap = mapOf(
                "firstName" to Contact::firstName.getter,
                "lastName" to Contact::lastName.getter,
                "fullName" to Contact::fullName.getter
            )
        }

        private val cache = Caffeine.newBuilder()
            .build<Contact, Int> { contact ->
                val count = fields.entries.sumBy { (name, values) ->
                    val field = fieldMap.getValue(name).invoke(contact)
                    values.count { field.contains(it, ignoreCase = true) }
                }
                if (count == 0) 0
                else {
                    val fieldValues =
                        listOf(contact.fullName) +
                                contact.phones.flatMap { listOf(it.label.name, it.phone) } +
                                contact.emails.flatMap { listOf(it.label.name, it.email) } +
                                contact.groups.map { it.name }

                    words.sumBy { w -> fieldValues.count { it.contains(w, ignoreCase = true) } } + count
                }
            }

        override val predicate: (Contact) -> Boolean = { cache[it]!! > 0 }
        override val comparator: Comparator<in Contact> = compareBy({ -cache[it]!! }, Contact::id)
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        init {
            logger.debug { "Supported contact fields in queries: ${ComplexQuery.fieldMap.keys}" }
        }

        @JvmStatic fun parse(query: String): ContactQuery {
            val theQuery = query.trim().let { str ->
                str.takeIf { it.count('"'::equals) % 2 == 0 } ?: str.lastIndexOf('"').let { i -> str.removeRange(i, i + 1) }
            }

            if (theQuery.replace(Regex("[\"\\s]"), "").isEmpty()) return EmptyQuery.also { logger.debug { "'$query' -> $it" } }

            val words = mutableSetOf<String>()
            val fields = mutableMapOf<String, MutableSet<String>>()

            lateinit var field: String

            var currState = State.NONE
            var fieldState = FieldState.NONE

            val buffer = StringBuilder()

            theQuery.forEachIndexed { i, c ->
                val prevState = currState
                when (c) {
                    ' ' -> {
                        if (prevState != QUOTED_LITERAL) currState = NONE
                        if (fieldState == NAME) fieldState = VALUE
                    }
                    '"' -> {
                        currState = when (prevState) {
                            NONE, LITERAL -> QUOTED_LITERAL
                            QUOTED_LITERAL -> NONE
                        }
                        if (fieldState == NAME) fieldState = VALUE
                    }
                    ':' -> if (prevState == LITERAL) fieldState = NAME
                    else -> {
                        when {
                            prevState == NONE -> currState = LITERAL
                            fieldState == NAME -> fieldState = VALUE
                        }
                    }
                }

                if ((currState == LITERAL || (currState == prevState && prevState == QUOTED_LITERAL)) && fieldState != NAME) {
                    buffer.append(c)
                }

                if (currState == LITERAL && fieldState == NAME) {
                    field = buffer.toString()
                    buffer.clear()
                }

                if ((currState == NONE && currState != prevState) || (prevState == LITERAL && currState == QUOTED_LITERAL) || i == theQuery.lastIndex) {
                    if (buffer.isNotEmpty()) {
                        if (fieldState == VALUE) {
                            field.takeIf { it in ComplexQuery.fieldMap.keys }?.let { fields.getOrPut(it) { mutableSetOf() }.add(buffer.toString()) }
                        } else {
                            words += buffer.toString()
                        }
                        buffer.clear()
                    }
                }
            }

            return (if (fields.isEmpty()) SimpleQuery(words) else ComplexQuery(words, fields)).also { logger.debug { "'$query' -> $it" } }
        }

        private enum class State { NONE, LITERAL, QUOTED_LITERAL }
        private enum class FieldState { NONE, NAME, VALUE }
    }
}
