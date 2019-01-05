package agenda.model.converter

import javafx.util.StringConverter

@Suppress("FunctionName")
inline fun <T : Any> StringConverter(
    crossinline toString: (T) -> String
): StringConverter<T> = object : StringConverter<T>() {
    override fun toString(obj: T): String = toString(obj)
    override fun fromString(str: String): T = throw UnsupportedOperationException ()
}

@Suppress("FunctionName")
inline fun <T : Any> StringConverter(
    crossinline toString: (T) -> String,
    crossinline fromString: (String) -> T
): StringConverter<T> = object : StringConverter<T>() {
    override fun toString(obj: T): String = toString(obj)
    override fun fromString(str: String): T = fromString(str)
}