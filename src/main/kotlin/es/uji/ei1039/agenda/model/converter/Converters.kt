package es.uji.ei1039.agenda.model.converter

import javafx.util.StringConverter

@Suppress("FunctionName")
inline fun <T : Any> StringConverter(crossinline convert: (T) -> String): StringConverter<T> = object : StringConverter<T>() {
    override fun toString(obj: T): String = convert(obj)
    override fun fromString(str: String): T = throw UnsupportedOperationException()
}
