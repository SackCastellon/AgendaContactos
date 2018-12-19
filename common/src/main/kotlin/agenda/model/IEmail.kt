package agenda.model

interface IEmail {
    val email: String
    val label: Label

    enum class Label {
        PERSONAL,
        WORK
    }
}
