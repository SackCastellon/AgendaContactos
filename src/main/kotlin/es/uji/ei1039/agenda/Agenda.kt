package es.uji.ei1039.agenda

import es.uji.ei1039.agenda.data.DatabaseManager
import es.uji.ei1039.agenda.util.Directories
import es.uji.ei1039.agenda.util.KoinContainer
import es.uji.ei1039.agenda.view.RootLayout
import es.uji.ei1039.agenda.view.styles.EditorStyles
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
