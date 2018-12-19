package agenda.model

interface IPhone {
    val phone: String
    val label: Label

    enum class Label {
        HOME,
        WORK,
        MOBILE
    }
}
