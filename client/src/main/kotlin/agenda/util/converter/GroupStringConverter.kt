package agenda.util.converter

import agenda.model.Group
import javafx.util.StringConverter

object GroupStringConverter : StringConverter<Group>() {
    override fun toString(group: Group): String = group.name
    override fun fromString(string: String): Group = throw UnsupportedOperationException()
}
