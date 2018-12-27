package es.uji.ei1039.agenda.util

import es.uji.ei1039.agenda.data.dao.*
import es.uji.ei1039.agenda.model.Contact
import es.uji.ei1039.agenda.model.Email
import es.uji.ei1039.agenda.model.Group
import es.uji.ei1039.agenda.model.Phone
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
