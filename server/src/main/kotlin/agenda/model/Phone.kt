package agenda.model

import agenda.model.IPhone.Label

data class Phone(
    override val phone: String,
    override val label: Label
) : IPhone
