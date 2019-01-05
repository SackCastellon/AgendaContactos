package agenda

import agenda.data.DatabaseManager
import agenda.util.Directories
import agenda.util.KoinContainer
import agenda.view.RootLayout
import agenda.view.styles.CommonStyles
import agenda.view.styles.EditorStyles
import tornadofx.App
import tornadofx.FX
import tornadofx.launch
import java.util.*

class Agenda : App(RootLayout::class, CommonStyles::class, EditorStyles::class) {
    override fun init() {
        FX.dicontainer = KoinContainer
        FX.messagesNameProvider = { "bundles.${it?.simpleName ?: "Messages"}" }

        // TODO Remove temporal fix
        val tmp = FX.locale
        FX.locale = Locale.ROOT
        FX.locale = tmp

        // Initialize directories
        Directories.create()
        // Initialize database manager
        DatabaseManager.setup()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>): Unit = launch<Agenda>(args)
    }
}
