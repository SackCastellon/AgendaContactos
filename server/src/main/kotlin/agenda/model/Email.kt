package agenda.model

import agenda.model.IEmail.Label

data class Email(
    override val email: String,
    override val label: Label
) : IEmail
