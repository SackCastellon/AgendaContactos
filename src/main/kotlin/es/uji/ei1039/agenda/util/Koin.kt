package es.uji.ei1039.agenda.util

import es.uji.ei1039.agenda.data.ContactRepository
import es.uji.ei1039.agenda.data.GroupRepository
import es.uji.ei1039.agenda.data.IRepository
import es.uji.ei1039.agenda.model.Contact
import es.uji.ei1039.agenda.model.Group
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
    single<IRepository<Contact>>("contacts") { ContactRepository() }
    single<IRepository<Group>>("groups") { GroupRepository() }
}
