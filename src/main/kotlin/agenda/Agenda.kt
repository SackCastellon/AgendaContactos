package agenda

import agenda.data.DatabaseManager
import agenda.util.Directories
import agenda.util.KoinContainer
import agenda.view.RootLayout
import agenda.view.styles.EditorStyles
import tornadofx.App
import tornadofx.FX
import tornadofx.launch

class Agenda : App(RootLayout::class, EditorStyles::class) {
    override fun init() {
        FX.dicontainer = KoinContainer

        // Initialize directories
        Directories
        // Initialize database manager
        DatabaseManager
    }
}

fun main(args: Array<String>): Unit = launch<Agenda>(args)
