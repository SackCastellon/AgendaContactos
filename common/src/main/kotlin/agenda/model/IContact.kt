package agenda.model

interface IContact {
    val name: String
    val surname: String
    val phones: List<IPhone>
    val emails: List<IEmail>
    val groups: List<IGroup>
}
