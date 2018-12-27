package agenda.util

import agenda.data.dao.*
import agenda.model.Contact
import agenda.model.Email
import agenda.model.Group
import agenda.model.Phone
import org.koin.dsl.module.module
import org.koin.log.Logger.SLF4JLogger
import org.koin.standalone.StandAloneContext.startKoin
import tornadofx.DIContainer
import kotlin.reflect.KClass

object KoinContainer : DIContainer {
    private val koin = startKoin(listOf(koinModule), logger = SLF4JLogger())

    override fun <T : Any> getInstance(type: KClass<T>): T = koin.koinContext.get(clazz = type)
    override fun <T : Any> getInstance(type: KClass<T>, name: String): T = koin.koinContext.get(name, type)
}

private val koinModule = module {
    single<IDao<Contact>>("contacts") { ContactsDao }
    single<IDao<Phone>>("phones") { PhonesDao }
    single<IDao<Email>>("emails") { EmailsDao }
    single<IDao<Group>>("groups") { GroupsDao }
}
