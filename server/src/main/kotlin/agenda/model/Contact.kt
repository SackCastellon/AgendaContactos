package agenda.model

data class Contact(
    override val name: String,
    override val surname: String,
    override val phones: List<IPhone>,
    override val emails: List<IEmail>,
    override val groups: List<IGroup>
) : IContact
